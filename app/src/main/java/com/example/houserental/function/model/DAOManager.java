package com.example.houserental.function.model;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class DAOManager {


    /* DEVICES */
    public static List<DeviceDAO> getAllDevices() {
        List<DeviceDAO> devices = new Select().from(DeviceDAO.class).orderBy("user").execute();
        if (devices == null)
            devices = new ArrayList<>();
        return devices;
    }

    public static List<DeviceDAO> getDevicesOfUser(Long user) {
        List<DeviceDAO> devices = new Select().from(DeviceDAO.class).where("user = ?", user).orderBy("mac").execute();
        if (devices == null)
            devices = new ArrayList<>();
        return devices;
    }

    public static List<DeviceDAO> getDevicesOfRoom(Long room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).execute();
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getId()));
            }
        }
        return devices;
    }

    public static List<DeviceDAO> getDevicesOfFloor(Long floor) {
        List<UserDAO> users = getUsersOfFloor(floor);
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getId()));
            }
        }
        return devices;
    }

    public static Long addDevice(String device, String description, Long user) {
        return new DeviceDAO(device, description, user).save();
    }

    public static void deleteDevice(String device) {
        new Delete().from(DeviceDAO.class).where("mac = ?", device).execute();
    }

    public static void updateDevice(Long id, String MAC, String description, Long user) {
        DeviceDAO device = new Select().from(DeviceDAO.class).where("id = ?", id).executeSingle();
        if (device != null) {
            device.setMAC(MAC);
            device.setUser(user);
            device.setDescription(description);
            device.save();
        }
    }

    public static DeviceDAO getDevice(String device) {
        return new Select().from(DeviceDAO.class).where("mac = ?", device).executeSingle();
    }

    public static boolean isDeviceExist(String device) {
        return new Select().from(DeviceDAO.class).where("mac = ?", device).exists();
    }

    /* USERS */
    public static List<UserDAO> getAllUsers() {
        List<UserDAO> users = new Select().from(UserDAO.class).orderBy("name").execute();
        if (users == null)
            users = new ArrayList<>();
        for (UserDAO user : users) {
            user.setDeviceCount(getDeviceCountOfUser(user.getId()));
            Calendar now = Calendar.getInstance();
            Calendar DOB = Calendar.getInstance();
            DOB.setTimeInMillis(user.getDOB().getTime());
            user.setAge(now.get(Calendar.YEAR) - DOB.get(Calendar.YEAR));
        }
        return users;
    }

    public static List<UserDAO> getUsersOfRoom(Long room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).orderBy("name").execute();
        if (users == null)
            users = new ArrayList<>();
        for (UserDAO user : users) {
            user.setDeviceCount(getDeviceCountOfUser(user.getId()));
            Calendar now = Calendar.getInstance();
            Calendar DOB = Calendar.getInstance();
            DOB.setTimeInMillis(user.getDOB().getTime());
            user.setAge(now.get(Calendar.YEAR) - DOB.get(Calendar.YEAR));
        }
        return users;
    }

    public static List<UserDAO> getUsersOfFloor(Long floor) {
        List<RoomDAO> rooms = getRoomsOfFloor(floor);
        List<UserDAO> users = new ArrayList<>();
        if (rooms != null && rooms.size() > 0) {
            for (RoomDAO room : rooms) {
                users.addAll(getUsersOfRoom(room.getId()));
            }
        }
        return users;
    }

    public static Long addUser(String identification, String name, int gender, Date DOB, UserDAO.Career career, String phone, Long room) {
        return new UserDAO(identification, name, gender, DOB, career, phone, room).save();
    }

    public static void deleteUser(Long user) {
        List<DeviceDAO> devices = getDevicesOfUser(user);
        for (DeviceDAO device : devices)
            device.delete();

        new Delete().from(UserDAO.class).where("id = ?", user).execute();
    }

    public static void updateUser(Long id, String identification, String name, int gender, Date DOB, UserDAO.Career career, String phone, Long room) {
        UserDAO user = new Select().from(UserDAO.class).where("id = ?", id).executeSingle();
        if (user != null) {
            user.setIdentification(identification);
            user.setName(name);
            user.setGender(gender);
            user.setCareer(career);
            user.setDOB(DOB);
            user.setRoom(room);
            user.setPhone(phone);
            user.save();
        }
    }

    public static UserDAO getUser(Long user) {
        return new Select().from(UserDAO.class).where("id = ?", user).executeSingle();
    }

    /* ROOMS */
    public static List<RoomDAO> getAllRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).orderBy("name").execute();
        if (rooms == null)
            rooms = new ArrayList<>();

        for (RoomDAO room : rooms) {
            room.setUserCount(getUserCountOfRoom(room.getId()));
            room.setDeviceCount(getDeviceCountOfRoom(room.getId()));
        }
        return rooms;
    }

    public static List<RoomDAO> getRoomsOfFloor(Long floor) {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("floor = ?", floor).orderBy("name").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        for (RoomDAO room : rooms) {
            room.setUserCount(getUserCountOfRoom(room.getId()));
            room.setDeviceCount(getDeviceCountOfRoom(room.getId()));
        }
        return rooms;
    }

    public static List<RoomDAO> getRentedRoomsOfFloor(Long floor) {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("rented = ? AND floor = ?", 1, floor).orderBy("name").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        for (RoomDAO room : rooms) {
            room.setUserCount(getUserCountOfRoom(room.getId()));
            room.setDeviceCount(getDeviceCountOfRoom(room.getId()));
        }
        return rooms;
    }

    public static Long addRoom(String name, int area, Long type_id, boolean rented, Date rent_date, int electric_number, int water_number, int deposit, Long floor) {
        return new RoomDAO(name, area, type_id, rented, rent_date, electric_number, water_number, deposit, floor).save();
    }

    public static void deleteRoom(Long id) {

        List<DeviceDAO> devices = getDevicesOfRoom(id);
        for (DeviceDAO device : devices)
            device.delete();

        List<UserDAO> users = getUsersOfRoom(id);
        for (UserDAO user : users)
            user.delete();

        new Delete().from(RoomDAO.class).where("id = ?", id).execute();
    }

    public static void updateRoom(Long id, String name, int area, Long type_id, boolean rented, Date rent_date, int electric_number, int water_number, int deposit, Long floor) {
        RoomDAO room = new Select().from(RoomDAO.class).where("id = ?", id).executeSingle();
        if (room != null) {
            room.setName(name);
            room.setArea(area);
            room.setType(type_id);
            room.setRented(rented);
            room.setRentDate(rent_date);
            room.setPaymentStartDate(rent_date);
            room.setFloor(floor);
            room.setElectricNumber(electric_number);
            room.setWaterNumber(water_number);
            room.setDeposit(deposit);
            room.save();
        }
    }

    public static RoomDAO getRoom(Long id) {
        return new Select().from(RoomDAO.class).where("id = ?", id).executeSingle();
    }

    public static List<RoomDAO> getAllRentedRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("rented = ?", 1).orderBy("name").execute();
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        return rooms;
    }

    public static void removeUsersOfRoom(Long room) {
        List<DeviceDAO> devices = getDevicesOfRoom(room);
        for (DeviceDAO device : devices)
            device.delete();
        List<UserDAO> users = getUsersOfRoom(room);
        for (UserDAO user : users)
            user.delete();
    }
    /* END ROOM */


    /* FLOORS */
    public static List<FloorDAO> getAllFloors() {
        List<FloorDAO> floors = new Select().from(FloorDAO.class).orderBy("floor_index").execute();
        if (floors == null)
            floors = new ArrayList<>();

        for (FloorDAO floor : floors) {
            floor.setRentedRoomCount(getRentedRoomCountOfFloor(floor.getId()));
            floor.setRoomCount(getRoomCountOfFloor(floor.getId()));
            floor.setDeviceCount(getDeviceCountOfFloor(floor.getId()));
            floor.setUserCount(getUserCountOfFloor(floor.getId()));
        }
        return floors;
    }

    public static Long addFloor(String name, int floor_index) {
        return new FloorDAO(name, floor_index).save();
    }

    public static void deleteFloor(Long floor) {

        List<DeviceDAO> devices = getDevicesOfFloor(floor);
        for (DeviceDAO device : devices)
            device.delete();

        List<UserDAO> users = getUsersOfFloor(floor);
        for (UserDAO user : users)
            user.delete();

        List<RoomDAO> rooms = getRoomsOfFloor(floor);
        for (RoomDAO room : rooms)
            room.delete();

        new Delete().from(FloorDAO.class).where("id = ?", floor).execute();
    }

    public static void updateFloor(Long id, String name, int floor_index) {
        FloorDAO floor = new Select().from(FloorDAO.class).where("id = ?", id).executeSingle();
        if (floor != null) {
            floor.setName(name);
            floor.setFloorIndex(floor_index);
            floor.save();
        }
    }

    public static FloorDAO getFloor(Long floor) {
        return new Select().from(FloorDAO.class).where("id = ?", floor).executeSingle();
    }

    public static int getNextFloorIndex() {
        FloorDAO floor = new Select().from(FloorDAO.class).orderBy("floor_index DESC").executeSingle();
        if (floor != null) {
            return floor.getFloorIndex() + 1;
        }
        return 0;
    }

    public static int getFloorCount() {
        return new Select().from(FloorDAO.class).count();
    }

    /* END FLOOR */

    /* COUNT */

    public static int getRoomCountOfRoomType(Long room_type) {
        return new Select().from(RoomDAO.class).where("type = ?", room_type).count();
    }

    public static int getRoomCountOfFloor(Long floor) {
        return new Select().from(RoomDAO.class).where("floor = ?", floor).count();
    }

    public static int getRentedRoomCountOfFloor(Long floor) {
        return new Select().from(RoomDAO.class).where("floor = ? AND rented = 1", floor).count();
    }

    public static int getUserCountOfFloor(Long floor) {
        return getUsersOfFloor(floor).size();
    }

    public static int getDeviceCountOfFloor(Long floor) {
        return getDevicesOfFloor(floor).size();
    }

    public static int getUserCountOfRoom(Long room) {
        return getUsersOfRoom(room).size();
    }

    public static int getDeviceCountOfRoom(Long room) {
        return getDevicesOfRoom(room).size();
    }

    public static int getDeviceCountOfUser(Long user) {
        return getDevicesOfUser(user).size();
    }

    /* END COUNT */

    /* PAYMENT */

    public static List<PaymentDAO> getAllPayments() {
        List<PaymentDAO> transactions = new Select().from(PaymentDAO.class).orderBy("created_date").execute();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    /* END PAYMENT */

    /* OWNER */

    public static List<OwnerDAO> getAllOwners() {
        List<OwnerDAO> owners = new Select().from(OwnerDAO.class).execute();
        if (owners == null)
            owners = new ArrayList<>();
        return owners;
    }

    public static OwnerDAO getOwner(Long id) {
        OwnerDAO owner = new Select().from(OwnerDAO.class).where("id = ?", id).executeSingle();
        return owner;
    }

    public static void addOwner(String name) {
        new OwnerDAO(name).save();
    }

    public static void deleteOwner(Long id) {
        new Delete().from(OwnerDAO.class).where("id = ?", id).execute();
    }

    /* END OWNER */

    /* ROOM TYPE */

    public static List<RoomTypeDAO> getAllRoomTypes() {
        List<RoomTypeDAO> types = new Select().from(RoomTypeDAO.class).orderBy("price ASC").execute();
        if (types == null)
            types = new ArrayList<>();

        for (RoomTypeDAO type : types) {
            type.setRoomCount(getRoomCountOfRoomType(type.getId()));
        }
        return types;
    }

    public static RoomTypeDAO getRoomType(Long id) {
        if (id == null)
            return null;
        RoomTypeDAO type = new Select().from(RoomTypeDAO.class).where("id = ?", id).executeSingle();
        type.setRoomCount(getRoomCountOfRoomType(type.getId()));
        return type;
    }

    public static void addRoomType(String name, int price) {
        new RoomTypeDAO(name, price).save();
    }

    public static void deleteRoomType(Long id) {
        new Delete().from(RoomTypeDAO.class).where("id = ?", id).execute();
    }

    public static void changeRoomType(Long from, Long to) {
        new Update(RoomDAO.class).set("type = ?", to).where("type = ?", from).execute();
    }

    public static int getRoomTypeCount() {
        return new Select().from(RoomTypeDAO.class).count();
    }
    /* END ROOM TYPE */
}

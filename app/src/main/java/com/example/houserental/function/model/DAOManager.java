package com.example.houserental.function.model;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class DAOManager {


    /* DEVICES */
    public synchronized static List<DeviceDAO> getAllDevices() {
        List<DeviceDAO> devices = new Select().from(DeviceDAO.class).orderBy("user").execute();
        if (devices == null)
            devices = new ArrayList<>();
        return devices;
    }

    public synchronized static List<DeviceDAO> getDevicesOfUser(Long user) {
        List<DeviceDAO> devices = new Select().from(DeviceDAO.class).where("user = ?", user).orderBy("mac").execute();
        if (devices == null)
            devices = new ArrayList<>();
        return devices;
    }

    public synchronized static List<DeviceDAO> getDevicesOfRoom(Long room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).execute();
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getId()));
            }
        }
        return devices;
    }

    public synchronized static List<DeviceDAO> getDevicesOfFloor(Long floor) {
        List<UserDAO> users = getUsersOfFloor(floor);
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getId()));
            }
        }
        return devices;
    }

    public synchronized static void addDevice(String device, String user) {
        new DeviceDAO(device, user).save();
    }

    public synchronized static void deleteDevice(String device) {
        new Delete().from(DeviceDAO.class).where("mac = ?", device).execute();
    }

    public synchronized static void updateDevice(Long id, String MAC, String user) {
        DeviceDAO device = new Select().from(DeviceDAO.class).where("id = ?", id).executeSingle();
        if (device != null) {
            device.setMAC(MAC);
            device.setUser(user);
            device.save();
        }
    }

    public synchronized static DeviceDAO getDevice(String device) {
        return new Select().from(DeviceDAO.class).where("mac = ?", device).executeSingle();
    }

    public synchronized static boolean isDeviceExist(String device) {
        return new Select().from(DeviceDAO.class).where("mac = ?", device).exists();
    }

    /* USERS */
    public synchronized static List<UserDAO> getAllUsers() {
        List<UserDAO> users = new Select().from(UserDAO.class).orderBy("name").execute();
        if (users == null)
            users = new ArrayList<>();
        return users;
    }

    public synchronized static List<UserDAO> getUsersOfRoom(Long room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).orderBy("name").execute();
        if (users == null)
            users = new ArrayList<>();
        return users;
    }

    public synchronized static List<UserDAO> getUsersOfFloor(Long floor) {
        List<RoomDAO> rooms = getRoomsOfFloor(floor);
        List<UserDAO> users = new ArrayList<>();
        if (rooms != null && rooms.size() > 0) {
            for (RoomDAO room : rooms) {
                users.addAll(getUsersOfRoom(room.getId()));
            }
        }
        return users;
    }

    public synchronized static Long addUser(String identification, String name, int gender, Date DOB, UserDAO.Career career, Long room) {
        return new UserDAO(identification, name, gender, DOB, career, room).save();
    }

    public synchronized static void deleteUser(Long user) {
        List<DeviceDAO> devices = getDevicesOfUser(user);
        for (DeviceDAO device : devices)
            device.delete();

        new Delete().from(UserDAO.class).where("id = ?", user).execute();
    }

    public synchronized static void updateUser(Long id, String identification, String name, int gender, Date DOB, UserDAO.Career career, Long room) {
        UserDAO user = new Select().from(UserDAO.class).where("id = ?", id).executeSingle();
        if (user != null) {
            user.setIdentification(identification);
            user.setName(name);
            user.setGender(gender);
            user.setCareer(career);
            user.setDOB(DOB);
            user.setRoom(room);
            user.save();
        }
    }

    public synchronized static UserDAO getUser(Long user) {
        return new Select().from(UserDAO.class).where("id = ?", user).executeSingle();
    }

    /* ROOMS */
    public synchronized static List<RoomDAO> getAllRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).orderBy("name").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        return rooms;
    }

    public synchronized static List<RoomDAO> getRoomsOfFloor(Long floor) {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("floor = ?", floor).orderBy("name").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        return rooms;
    }

    public synchronized static Long addRoom(String name, int area, Long type_id, boolean rented, Date rent_date, int electric_number, int water_number, Long floor) {
        return new RoomDAO(name, area, type_id, rented, rent_date, electric_number, water_number, floor).save();
    }

    public synchronized static void deleteRoom(Long id) {

        List<DeviceDAO> devices = getDevicesOfRoom(id);
        for (DeviceDAO device : devices)
            device.delete();

        List<UserDAO> users = getUsersOfRoom(id);
        for (UserDAO user : users)
            user.delete();

        new Delete().from(RoomDAO.class).where("id = ?", id).execute();
    }

    public synchronized static void updateRoom(Long id, String name, int area, Long type_id, boolean rented, Date rent_date, int electric_number, int water_number, Long floor) {
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
            room.save();
        }
    }

    public synchronized static RoomDAO getRoom(Long id) {
        return new Select().from(RoomDAO.class).where("id = ?", id).executeSingle();
    }

    public synchronized static List<RoomDAO> getAllRentedRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("rented = ?", 1).orderBy("id").execute();
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        return rooms;
    }

    /* FLOORS */
    public synchronized static List<FloorDAO> getAllFloors() {
        List<FloorDAO> floors = new Select().from(FloorDAO.class).orderBy("floor_index").execute();
        if (floors == null)
            floors = new ArrayList<>();
        return floors;
    }

    public synchronized static Long addFloor(String name, int floor_index) {
        return new FloorDAO(name, floor_index).save();
    }

    public synchronized static void deleteFloor(Long floor) {

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

    public synchronized static void updateFloor(Long id, String name, int floor_index) {
        FloorDAO floor = new Select().from(FloorDAO.class).where("id = ?", id).executeSingle();
        if (floor != null) {
            floor.setName(name);
            floor.setFloorIndex(floor_index);
            floor.save();
        }
    }

    public synchronized static FloorDAO getFloor(Long floor) {
        return new Select().from(FloorDAO.class).where("id = ?", floor).executeSingle();
    }

    /* COUNT */

    public synchronized static int getRoomCountOfFloor(Long floor) {
        return new Select().from(RoomDAO.class).where("floor = ?", floor).count();
    }

    public synchronized static int getUserCountOfFloor(Long floor) {
        return getUsersOfFloor(floor).size();
    }

    public synchronized static int getDeviceCountOfFloor(Long floor) {
        return getDevicesOfFloor(floor).size();
    }

    public synchronized static int getUserCountOfRoom(Long room) {
        return getUsersOfRoom(room).size();
    }

    public synchronized static int getDeviceCountOfRoom(Long room) {
        return getDevicesOfRoom(room).size();
    }

    public synchronized static List<PaymentDAO> getAllPayments() {
        List<PaymentDAO> transactions = new Select().from(PaymentDAO.class).orderBy("created_date").execute();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    /* END COUNT */

    /* PAYMENT */

    public synchronized static List<OwnerDAO> getAllOwners() {
        List<OwnerDAO> owners = new Select().from(OwnerDAO.class).execute();
        if (owners == null)
            owners = new ArrayList<>();
        return owners;
    }

    public synchronized static void addOwner(String name) {
        new OwnerDAO(name).save();
    }

    /* END PAYMENT */

    /* OWNER */

    public synchronized static void deleteOwner(Long id) {
        new Delete().from(OwnerDAO.class).where("id = ?", id).execute();
    }

    public synchronized static List<RoomTypeDAO> getAllRoomTypes() {
        List<RoomTypeDAO> types = new Select().from(RoomTypeDAO.class).orderBy("price ASC").execute();
        if (types == null)
            types = new ArrayList<>();

        return types;
    }

    public synchronized static RoomTypeDAO getRoomType(Long id) {
        if (id == null)
            return null;
        RoomTypeDAO type = new Select().from(RoomTypeDAO.class).where("id = ?", id).executeSingle();
        return type;
    }

    /* END OWNER */

    /* ROOM TYPE */

    public synchronized static void addRoomType(String name, int price) {
        new RoomTypeDAO(name, price).save();
    }

    public synchronized static void deleteRoomType(Long id) {
        new Update(RoomDAO.class).set("type = ?", -1).where("type = ?", id).execute();
        new Delete().from(RoomTypeDAO.class).where("id = ?", id).execute();
    }

    public synchronized int getDeviceCountOfUser(Long user) {
        return getDevicesOfUser(user).size();
    }

    /* END ROOM TYPE */
}

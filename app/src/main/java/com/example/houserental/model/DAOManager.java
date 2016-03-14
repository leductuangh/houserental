package com.example.houserental.model;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

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

    public synchronized static List<DeviceDAO> getDevicesOfUser(String user) {
        List<DeviceDAO> devices = new Select().from(DeviceDAO.class).where("user = ?", user).orderBy("mac").execute();
        if (devices == null)
            devices = new ArrayList<>();
        return devices;
    }

    public synchronized static List<DeviceDAO> getDevicesOfRoom(String room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).execute();
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getUserId()));
            }
        }
        return devices;
    }

    public synchronized static List<DeviceDAO> getDevicesOfFloor(String floor) {
        List<UserDAO> users = getUsersOfFloor(floor);
        List<DeviceDAO> devices = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (UserDAO user : users) {
                devices.addAll(getDevicesOfUser(user.getUserId()));
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

    public synchronized static List<UserDAO> getUsersOfRoom(String room) {
        List<UserDAO> users = new Select().from(UserDAO.class).where("room = ?", room).orderBy("name").execute();
        if (users == null)
            users = new ArrayList<>();
        return users;
    }

    public synchronized static List<UserDAO> getUsersOfFloor(String floor) {
        List<RoomDAO> rooms = getRoomsOfFloor(floor);
        List<UserDAO> users = new ArrayList<>();
        if (rooms != null && rooms.size() > 0) {
            for (RoomDAO room : rooms) {
                users.addAll(getUsersOfRoom(room.getRoomId()));
            }
        }
        return users;
    }

    public synchronized static void addUser(String id, String name, int gender, Date DOB, UserDAO.Career career, String room) {
        new UserDAO(id, name, gender, DOB, career, room).save();
    }

    public synchronized static void deleteUser(String user) {
        List<DeviceDAO> devices = getDevicesOfUser(user);
        for (DeviceDAO device : devices)
            device.delete();

        new Delete().from(UserDAO.class).where("user_id = ?", user).execute();
    }

    public synchronized static void updateUser(Long id, String user_id, String name, int gender, Date DOB, UserDAO.Career career, String room) {
        UserDAO user = new Select().from(UserDAO.class).where("id = ?", id).executeSingle();
        if (user != null) {
            user.setUserId(user_id);
            user.setName(name);
            user.setGender(gender);
            user.setCareer(career);
            user.setDOB(DOB);
            user.setRoom(room);
            user.save();
        }
    }

    public synchronized static UserDAO getUser(String user) {
        return new Select().from(UserDAO.class).where("user_id = ?", user).executeSingle();
    }

    /* ROOMS */
    public synchronized static List<RoomDAO> getAllRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).orderBy("room_id").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        return rooms;
    }

    public synchronized static List<RoomDAO> getRoomsOfFloor(String floor) {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("floor = ?", floor).orderBy("room_id").execute();
        if (rooms == null)
            rooms = new ArrayList<>();
        return rooms;
    }

    public synchronized static void addRoom(String id, String name, int area, RoomDAO.Type type, boolean rented, Date rent_date, int electric_number, int water_number, String floor) {
        new RoomDAO(id, name, area, type, rented, rent_date, electric_number, water_number, floor).save();
    }

    public synchronized static void deleteRoom(String room) {

        List<DeviceDAO> devices = getDevicesOfRoom(room);
        for (DeviceDAO device : devices)
            device.delete();

        List<UserDAO> users = getUsersOfRoom(room);
        for (UserDAO user : users)
            user.delete();

        new Delete().from(RoomDAO.class).where("room_id = ?", room).execute();
    }

    public synchronized static void updateRoom(Long id, String room_id, String name, int area, RoomDAO.Type type, boolean rented, Date rent_date, int electric_number, int water_number, String floor) {
        RoomDAO room = new Select().from(RoomDAO.class).where("id = ?", id).executeSingle();
        if (room != null) {
            room.setRoomId(room_id);
            room.setName(name);
            room.setArea(area);
            room.setType(type);
            room.setRented(rented);
            room.setRentDate(rent_date);
            room.setFloor(floor);
            room.setElectricNumber(electric_number);
            room.setWaterNumber(water_number);
            room.save();
        }
    }

    public synchronized static RoomDAO getRoom(String room) {
        return new Select().from(RoomDAO.class).where("room_id = ?", room).executeSingle();
    }

    public synchronized static int getUserCountOfRoom(String room) {
        return getUsersOfRoom(room).size();
    }

    public synchronized static int getDeviceCountOfRoom(String room) {
        return getDevicesOfRoom(room).size();
    }

    public synchronized static List<RoomDAO> getAllRentedRooms() {
        List<RoomDAO> rooms = new Select().from(RoomDAO.class).where("rented = ?", 1).orderBy("room_id").execute();
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

    public synchronized static void addFloor(String id, String name, int floor_index) {
        new FloorDAO(id, name, floor_index).save();
    }

    public synchronized static void deleteFloor(String floor) {

        List<DeviceDAO> devices = getDevicesOfFloor(floor);
        for (DeviceDAO device : devices)
            device.delete();

        List<UserDAO> users = getUsersOfFloor(floor);
        for (UserDAO user : users)
            user.delete();

        List<RoomDAO> rooms = getRoomsOfFloor(floor);
        for (RoomDAO room : rooms)
            room.delete();

        new Delete().from(FloorDAO.class).where("floor_id = ?", floor).execute();
    }

    public synchronized static void updateFloor(Long id, String floor_id, String name, int floor_index) {
        FloorDAO floor = new Select().from(FloorDAO.class).where("id = ?", id).executeSingle();
        if (floor != null) {
            floor.setFloorId(floor_id);
            floor.setName(name);
            floor.setFloorIndex(floor_index);
            floor.save();
        }
    }

    public synchronized static FloorDAO getFloor(String floor) {
        return new Select().from(FloorDAO.class).where("floor_id = ?", floor).executeSingle();
    }

    public synchronized static int getRoomCountOfFloor(String floor) {
        return new Select().from(RoomDAO.class).where("floor = ?", floor).count();
    }

    public synchronized static int getUserCountOfFloor(String floor) {
        return getUsersOfFloor(floor).size();
    }

    public synchronized static int getDeviceCountOfFloor(String floor) {
        return getDevicesOfFloor(floor).size();
    }

    public synchronized static List<TransactionDAO> getAllTransactions() {
        List<TransactionDAO> transactions = new Select().from(TransactionDAO.class).orderBy("created_date").execute();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    /* TRANSACTION */

    public synchronized int getDeviceCountOfUser(String user) {
        return getDevicesOfUser(user).size();
    }
}

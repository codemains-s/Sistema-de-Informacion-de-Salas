from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from config.db import Base

class User(Base):
    __tablename__ = 'users'
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(254))
    email = Column(String(100))
    password = Column(String(100))
    phone = Column(String(20))
    role_id = Column(Integer, ForeignKey('roles.id'), unique=True)
    
    role = relationship('Role', back_populates='users', uselist=False)
    rooms = relationship('UserRoom', back_populates='user')

class Role(Base):
    __tablename__ = 'roles'
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100))
    
    users = relationship('User', back_populates='role', uselist=False)

class Room(Base):
    __tablename__ = 'rooms'
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100))
    address = Column(String(254))
    description = Column(String(254))
    capacity = Column(Integer)
    status = Column(String(100))
    image = Column(String(500))
    
    schedules = relationship('RoomSchedule', back_populates='room')
    users = relationship('UserRoom', back_populates='room')

class Schedule(Base):
    __tablename__ = 'schedules'
    
    id = Column(Integer, primary_key=True, index=True)
    day = Column(String(100))
    start_time = Column(String(100))
    end_time = Column(String(100))
    
    rooms = relationship('RoomSchedule', back_populates='schedule')

class RoomSchedule(Base):
    __tablename__ = 'roomschedule'
    
    id = Column(Integer, primary_key=True, index=True)
    room_id = Column(Integer, ForeignKey('rooms.id'))
    schedule_id = Column(Integer, ForeignKey('schedules.id'))
    date = Column(String(100))
    hour = Column(String(100))
    
    room = relationship('Room', back_populates='schedules')
    schedule = relationship('Schedule', back_populates='rooms')

class UserRoom(Base):
    __tablename__ = 'userrooms'
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    room_id = Column(Integer, ForeignKey('rooms.id'))
    date = Column(String(100))
    hour = Column(String(100))
    
    user = relationship('User', back_populates='rooms')
    room = relationship('Room', back_populates='users')

from sqlalchemy import Column, Integer, String, ForeignKey, Date, Float
from sqlalchemy.orm import relationship
from config.db import Base
from datetime import datetime


class Program(Base): # Listo ---------------------------------------------
    __tablename__ = 'program'
    
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    description = Column(String(1023))
    
    # Relación uno a muchos con users
    users = relationship('User', back_populates='program')

class User(Base): # Listo ---------------------------------------------
    __tablename__ = 'users'
    
    id = Column(Integer, primary_key=True)
    program_id = Column(Integer, ForeignKey('program.id'), nullable=False)
    role_id = Column(Integer, ForeignKey('roles.id'), nullable=False)
    name = Column(String(100), nullable=False)
    email = Column(String(100), nullable=False, unique=True)
    password = Column(String(255), nullable=False)
    token = Column(String(255))
    phone = Column(String(20))
    
    # Relaciones
    program = relationship('Program', back_populates='users')
    role = relationship('Role', back_populates='users')
    room_bookings = relationship('RoomBooking', back_populates='user')
    completed_hours = relationship('CompletedHours', back_populates='user')

class Role(Base): # Listo ---------------------------------------------
    __tablename__ = 'roles'
    
    id = Column(Integer, primary_key=True)
    name = Column(String(50), nullable=False)
 
    users = relationship('User', back_populates='role', uselist=False)

class Room(Base): #Listo ---------------------------------------------
    __tablename__ = 'rooms'
    
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    address = Column(String(200))
    description = Column(String(500))
    capacity = Column(Integer)
    status = Column(String(50))
    image = Column(String(255))
    
    # Relaciones
    room_bookings = relationship('RoomBooking', back_populates='room')
    room_schedules = relationship('RoomSchedule', back_populates='room')

class RoomBooking(Base): # Listo ---------------------------------------------
    __tablename__ = 'room_bookings'
    
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    room_id = Column(Integer, ForeignKey('rooms.id'), nullable=False)
    start_time = Column(String(100), nullable=False)
    end_time = Column(String(100), nullable=False)
    status = Column(String(100))
    booking_date = Column(String(100), default=datetime.utcnow)
    
    # Relaciones
    user = relationship('User', back_populates='room_bookings')
    room = relationship('Room', back_populates='room_bookings')
    completed_hours = relationship('CompletedHours', back_populates='room_booking')

class RoomSchedule(Base): # Listo ---------------------------------------------
    __tablename__ = 'room_schedule'
    
    id = Column(Integer, primary_key=True)
    room_id = Column(Integer, ForeignKey('rooms.id'), nullable=False)
    hour_start = Column(String(50))  # Formato "HH:MM"
    hour_end = Column(String(50))    # Formato "HH:MM"
    day_of_week = Column(String(50))    # 1-7 para Lunes-Domingo
    status = Column(String(50))
    room = relationship('Room', back_populates='room_schedules')

class CompletedHours(Base):
    __tablename__ = 'completed_hours'
    
    id = Column(Integer, primary_key=True)
    room_booking_id = Column(Integer, ForeignKey('room_bookings.id'), nullable=False)
    user_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    hour_completed = Column(Integer)
    date_register = Column(String(100), default=datetime.utcnow)
    status = Column(String(50))
    signature = Column(String(1023))
    observations = Column(String(1023))
    
    # Relaciones
    room_booking = relationship('RoomBooking', back_populates='completed_hours')
    user = relationship('User', back_populates='completed_hours')





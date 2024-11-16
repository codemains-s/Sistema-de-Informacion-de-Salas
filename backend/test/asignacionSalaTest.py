import unittest
from fastapi import HTTPException
from sqlalchemy.orm import Session
from datetime import datetime
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from config.db import get_db
from routes.roomBookingRoutes import new_roomBooking
from schemas.roomBooking import RoomBooking

class TestRoomBooking(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db: Session = next(get_db())

    def test_AS_01(self):
        """Realizar una asignación de una sala a un usuario monitor, dentro de un periodo de tiempo válido."""
        booking = RoomBooking(
            user_id=1,
            room_id=1,
            start_time="08:00 AM",
            end_time="10:00 AM",
            status="Active",
            booking_date="16/11/2024"
        )
        response = new_roomBooking(booking, self.db)
        self.assertEqual(response, "Sala asignada al estudiante de manera correcta")
        

    def test_AS_02(self):
        """Realizar una asignación de una sala a un usuario monitor, haciendo que la hora final sea más temprana que la hora inicial."""
        booking = RoomBooking(
            user_id=1,
            room_id=1,
            start_time="12:00 AM",
            end_time="10:00 PM",
            status="Active",
            booking_date="16/11/2024"
        )
        response = new_roomBooking(booking, self.db)
        self.assertEqual(response, "Horas de monitorias son inválidas")

    def test_AS_03(self):
        """Realizar una asignación de una sala a un usuario monitor que no se encuentra inscrito en el sistema."""
        booking = RoomBooking(
            user_id=9999,
            room_id=1,
            start_time="2:00 PM",
            end_time="4:00 PM",
            status="Active",
            booking_date="18/11/2024"
        )
        response = new_roomBooking(booking, self.db)
        self.assertEqual(response, "Usuario ingresado no existe")

    def test_AS_04(self):
        """Realizar una asignación de una sala a un usuario monitor, haciendo que la fecha de ingreso sea una que ya pasó."""
        booking = RoomBooking(
            user_id=1,
            room_id=1,
            start_time="2:00 PM",
            end_time="4:00 PM",
            status="Active",
            booking_date="05/01/2023" 
        )
        response = new_roomBooking(booking, self.db)
        self.assertEqual(response, "Fecha no válida")

    def test_AS_05(self):
        """Realizar una asignación de una sala a un usuario monitor, haciendo que las horas de monitoría no estén en un rango aceptado (madrugada)."""
        booking = RoomBooking(
            user_id=1,
            room_id=1,
            start_time="01:00 AM",
            end_time="03:00 AM",  
            status="Active",
            booking_date="2024-11-18"
        )
        response = new_roomBooking(booking, self.db)
        self.assertEqual(response, "Horas de monitorias son inválidas")

if __name__ == '__main__':
    unittest.main()

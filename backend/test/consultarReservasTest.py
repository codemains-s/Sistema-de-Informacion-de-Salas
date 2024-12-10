import unittest
from fastapi import HTTPException
from sqlalchemy.orm import Session
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from config.db import get_db
from routes.userRoutes import get_user_by_id_endpoint, all_users_role_id, get_monitor_by_id_endpoint
from routes.roomBookingRoutes import get_roomBookings_user

class ConsultarReservasTest(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db: Session = next(get_db())
    
    def test_CR_01(self):
        """Se consulta una reserva de una sala con el id de un usuario que existe y que tiene una reserva {id_user = 3}"""
        id_user = 3
        response = get_roomBookings_user(id_user, self.db)
        self.assertTrue(len(response) > 0)

    def test_CR_02(self):
        """Se hace una consulta de una reserva a un usuario existente pero que no tiene ninguna reserva disponible {id_user = 5}"""
        id_user = 5
        response = get_roomBookings_user(id_user, self.db)
        self.assertEquals(response, f"No se encontraron reservas para el usuario con ID {id_user}.")

    def test_CR_03(self):
        """Se hace una consulta de una reserva a un usuario que no existe en la base de datos"""
        id_user = 9999
        response = get_roomBookings_user(id_user, self.db)
        self.assertEquals(response, f"No se encontraron reservas para el usuario con ID {id_user}.")

    def test_CR_04(self):
        """Se hace una consulta a una reserva con un usuario que no es monitor"""
        id_user = 9999
        response = get_roomBookings_user(id_user, self.db)
        self.assertEquals(response, f"No se encontraron reservas para el usuario con ID {id_user}.")

    def test_CR_05(self):
        """Se hace una consulta a una reserva ingresando un id de usuario diferente a un número, por ejemplo una cadena de texto"""
        id_user = "texto"
        response = get_roomBookings_user(id_user, self.db)
        self.assertEquals(response, f"No se encontraron reservas para el usuario con ID {id_user}.")
        
if __name__ == '__main__':
    unittest.main()
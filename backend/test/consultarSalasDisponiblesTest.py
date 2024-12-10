import unittest
from sqlalchemy.orm import Session
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from config.db import get_db
from routes.roomRoutes import get_room_by_status_endpoint


class TestRoomAvailable(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db: Session = next(get_db())
    
    def test_RA_01(self):
        """Consultar salas con estado Disponible."""
        response = get_room_by_status_endpoint("Disponible", self.db)
        self.assertEqual(response, [
            {
                "name": "Sala L",
                "status": "Disponible",
            },
            
        ])

    def test_RA_02(self):
        """Consultar salas con estado Reservada."""
        response = get_room_by_status_endpoint("Reservada", self.db)
        self.assertEqual(response, [])

    def test_RA_03(self):
        """Consultar salas sin estado."""
        with self.assertRaises(ValueError) as context:
            get_room_by_status_endpoint("", self.db)
        
        # Verificar el mensaje de la excepción
        self.assertEqual(str(context.exception), "Se debe de ingresar un estado de sala")
        
        
if __name__ == '__main__':
    unittest.main()
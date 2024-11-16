import unittest
from sqlalchemy.orm import Session
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from config.db import get_db, Base, engine
from routes.roomRoutes import get_room_by_name_endpoint as get_room_by_name

class TestConsultarSalaCodigo(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db = next(get_db())

    def test_CS_01(self):
        """Se realiza la búsqueda de una sala ingresando el nombre asignado a esta sala."""
        room = get_room_by_name("Sala J", self.db)
        self.assertIsNotNone(room)
        self.assertEqual(room.name, "Sala J")

    def test_CS_02(self):
        """Se realiza la búsqueda de una sala ingresando un nombre que no se encuentra en el sistema."""
        room = get_room_by_name("Sala Inexistente", self.db)
        self.assertEqual(room, {"error": "Room not found"})

    def test_CS_03(self):
        """Se realiza la búsqueda de una sala ingresando un nombre de sala vacío o con espacios en blanco."""
        room = get_room_by_name("", self.db)
        self.assertEqual(room, {"error": "Room not found"})

    def test_CS_04(self):
        """Se realiza la búsqueda de una sala ingresando un nombre con mayúsculas."""
        room = get_room_by_name("SALA J", self.db)
        self.assertIsNotNone(room)
        self.assertEqual(room.name, "Sala J")

    def test_CS_05(self):
        """Se realiza la búsqueda de una sala ingresando un dato distinto a una cadena de texto."""
        with self.assertRaises(AttributeError):
            get_room_by_name([], self.db)

if __name__ == '__main__':
    print("Pruebas para el ítem consultar sala por código")
    unittest.main()
import unittest
from sqlalchemy.orm import Session
import sys
import os
from datetime import datetime

# Ajusta la ruta para que el archivo de prueba pueda importar desde la raíz del proyecto
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))

# Importar la configuración de la base de datos y las funciones del controlador
from config.db import get_db
from schemas.completedHour import CompletedHour
from controllers.completedHourController import (
    create_completed_hour,
    all_completed_hours,
    get_completed_hour_by_id,
    get_completed_hour_by_user_id,
    update_completed_hour,
    delete_completed_hour,
    exist_completed_hour,
)

class TestCompletedHours(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db: Session = next(get_db())

    def test_create_completed_hour(self):
        """Crear un registro de hora completada."""
        new_hour = CompletedHour(
            room_booking_id=2,
            user_id=1,
            hour_completed=4,
            date_register="2024-11-17",
            status="Pending",
            observations="Test observation"
        )
        response = create_completed_hour(new_hour, self.db)
        self.assertIsNotNone(response.id)
        self.assertEqual(response.room_booking_id, 2)
        self.assertEqual(response.status, "Pending")
        self.assertEqual(response.observations, "Test observation")

    def test_all_completed_hours(self):
        """Obtener todos los registros de horas completadas."""
        response = all_completed_hours(self.db)
        self.assertGreaterEqual(len(response), 1)  # Debe haber al menos un registro

    def test_get_completed_hour_by_id(self):
        """Obtener un registro por su ID."""
        response = get_completed_hour_by_id(1, self.db)
        self.assertIsNotNone(response)
        self.assertEqual(response.id, 1)

    def test_get_completed_hour_by_user_id(self):
        """Obtener registros por el ID del usuario."""
        response = get_completed_hour_by_user_id(1, self.db)
        self.assertGreaterEqual(len(response), 1)
        self.assertEqual(response[0].user_id, 1)

    def test_update_completed_hour(self):
        """Actualizar un registro existente."""
        updated_hour = CompletedHour(
            room_booking_id=2,
            user_id=1,
            hour_completed=10,
            date_register="2024-11-18",
            status="Updated",
            observations="Updated observation"
        )
        response = update_completed_hour(1, updated_hour, self.db)
        self.assertEqual(response.hour_completed, 10)
        self.assertEqual(response.status, "Updated")
        self.assertEqual(response.observations, "Updated observation")

    def test_delete_completed_hour(self):
        """Eliminar un registro por su ID."""
        response = delete_completed_hour(3, self.db)
        self.assertIsNone(response)
        self.assertIsNone(get_completed_hour_by_id(3, self.db))

    def test_exist_completed_hour(self):
        """Verificar si un registro existe por su ID."""
        response = exist_completed_hour(999, self.db)  # ID que no existe
        self.assertIsNone(response)

if __name__ == '__main__':
    unittest.main()

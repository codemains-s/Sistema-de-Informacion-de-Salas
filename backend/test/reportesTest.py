import unittest
from unittest.mock import MagicMock
from io import BytesIO
import pandas as pd
from controllers.reportController import (
    generate_user_report,
)


class TestGenerateUserReport(unittest.TestCase):
    def setUp(self):
        self.mock_db = MagicMock()

    def test_generate_report_user_exists(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        mock_booking = MagicMock()
        mock_booking.booking_date = "2024-12-09"
        mock_booking.room.name = "Room A"
        mock_booking.start_time = "10:00"
        mock_booking.end_time = "11:00"
        mock_booking.user_id = 1

        mock_completed_hour = MagicMock()
        mock_completed_hour.hour_completed = 2
        mock_completed_hour.observations = "Completed task"

        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            [mock_booking],
            [mock_completed_hour],
        ]

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 1)
        self.assertEqual(output_df["Nombre"][0], "John Doe")
        self.assertEqual(output_df["Horas Completadas"][0], 2)
        self.assertEqual(output_df["Sala"][0], "Room A")
        self.assertEqual(output_df["Fecha"][0], "2024-12-09")
        self.assertEqual(output_df["Hora Inicio"][0], "10:00")
        self.assertEqual(output_df["Hora Fin"][0], "11:00")
        self.assertEqual(output_df["Observaciones"][0], "Completed task")

    def test_generate_report_user_not_found(self):
        self.mock_db.query.return_value.filter.return_value.first.return_value = None

        result = generate_user_report(999, self.mock_db)

        self.assertIsNone(result)

    def test_generate_report_no_bookings_or_hours(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.return_value = []

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 0)

    def test_generate_report_more_completed_hours_than_bookings(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        mock_booking = MagicMock()
        mock_booking.booking_date = "2024-12-09"
        mock_booking.room.name = "Room A"
        mock_booking.start_time = "10:00"
        mock_booking.end_time = "11:00"
        mock_booking.user_id = 1

        mock_completed_hour = MagicMock()
        mock_completed_hour.hour_completed = 5
        mock_completed_hour.observations = "Completed task"

        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            [mock_booking],
            [mock_completed_hour],
        ]

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 1)
        self.assertEqual(output_df["Horas Completadas"][0], 5)

    def test_generate_report_invalid_booking_date(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        mock_booking = MagicMock()
        mock_booking.booking_date = None
        mock_booking.room.name = "Room A"
        mock_booking.start_time = "10:00"
        mock_booking.end_time = "11:00"
        mock_booking.user_id = 1

        mock_completed_hour = MagicMock()
        mock_completed_hour.hour_completed = 2
        mock_completed_hour.observations = "Completed task"

        # Configurar la base de datos mock para devolver los valores
        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            [mock_booking],
            [mock_completed_hour],
        ]

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 1)
        self.assertTrue(output_df["Fecha"][0])
        self.assertEqual(output_df["Horas Completadas"][0], 2)

    def test_generate_report_user_without_bookings_or_completed_hours(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            [],
            [],
        ]

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(
            len(output_df), 0
        )  # No debe haber filas, ya que no hay reservas ni horas

    def test_generate_report_multiple_bookings_and_completed_hours(self):
        mock_user = MagicMock()
        mock_user.id = 1
        mock_user.name = "John Doe"

        mock_booking1 = MagicMock()
        mock_booking1.booking_date = "2024-12-09"
        mock_booking1.room.name = "Room A"
        mock_booking1.start_time = "10:00"
        mock_booking1.end_time = "11:00"
        mock_booking1.user_id = 1

        mock_booking2 = MagicMock()
        mock_booking2.booking_date = "2024-12-10"
        mock_booking2.room.name = "Room B"
        mock_booking2.start_time = "14:00"
        mock_booking2.end_time = "15:00"
        mock_booking2.user_id = 1

        mock_completed_hour1 = MagicMock()
        mock_completed_hour1.hour_completed = 2
        mock_completed_hour1.observations = "Completed task 1"

        mock_completed_hour2 = MagicMock()
        mock_completed_hour2.hour_completed = 3
        mock_completed_hour2.observations = "Completed task 2"

        self.mock_db.query.return_value.filter.return_value.first.return_value = (
            mock_user
        )
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            [mock_booking1, mock_booking2],
            [
                mock_completed_hour1,
                mock_completed_hour2,
            ],
        ]

        result = generate_user_report(1, self.mock_db)

        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 2)
        self.assertEqual(output_df["Horas Completadas"][0], 2)
        self.assertEqual(output_df["Horas Completadas"][1], 5)
        self.assertEqual(output_df["Sala"][0], "Room A")
        self.assertEqual(output_df["Sala"][1], "Room B")

    def tearDown(self):
        self.mock_db.reset_mock()


if __name__ == "__main__":
    unittest.main()

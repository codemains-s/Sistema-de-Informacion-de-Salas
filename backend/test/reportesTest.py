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

    def _create_mock_user(self, user_id=1, name="John Doe"):
        mock_user = MagicMock()
        mock_user.id = user_id
        mock_user.name = name
        return mock_user

    def _create_mock_booking(
        self, booking_date, room_name, start_time, end_time, user_id=1
    ):
        mock_booking = MagicMock()
        mock_booking.booking_date = booking_date
        mock_booking.room.name = room_name
        mock_booking.start_time = start_time
        mock_booking.end_time = end_time
        mock_booking.user_id = user_id
        return mock_booking

    def _create_mock_completed_hour(self, hours_completed, observations):
        mock_completed_hour = MagicMock()
        mock_completed_hour.hour_completed = hours_completed
        mock_completed_hour.observations = observations
        return mock_completed_hour

    def _mock_db_responses(self, user=None, bookings=None, completed_hours=None):
        self.mock_db.query.return_value.filter.return_value.first.return_value = user
        self.mock_db.query.return_value.filter.return_value.all.side_effect = [
            bookings or [],
            completed_hours or [],
        ]

    def test_generate_report_user_exists(self):
        mock_user = self._create_mock_user()
        mock_booking = self._create_mock_booking(
            "2024-12-09", "Room A", "10:00", "11:00"
        )
        mock_completed_hour = self._create_mock_completed_hour(2, "Completed task")

        self._mock_db_responses(
            user=mock_user,
            bookings=[mock_booking],
            completed_hours=[mock_completed_hour],
        )

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
        self._mock_db_responses(user=None)
        result = generate_user_report(999, self.mock_db)
        self.assertIsNone(result)

    def test_generate_report_no_bookings_or_hours(self):
        mock_user = self._create_mock_user()
        self._mock_db_responses(user=mock_user)
        result = generate_user_report(1, self.mock_db)
        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 0)

    def test_generate_report_more_completed_hours_than_bookings(self):
        mock_user = self._create_mock_user()
        mock_booking = self._create_mock_booking(
            "2024-12-09", "Room A", "10:00", "11:00"
        )
        mock_completed_hour = self._create_mock_completed_hour(5, "Completed task")

        self._mock_db_responses(
            user=mock_user,
            bookings=[mock_booking],
            completed_hours=[mock_completed_hour],
        )

        result = generate_user_report(1, self.mock_db)
        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 1)
        self.assertEqual(output_df["Horas Completadas"][0], 5)

    def test_generate_report_invalid_booking_date(self):
        mock_user = self._create_mock_user()
        mock_booking = self._create_mock_booking(None, "Room A", "10:00", "11:00")
        mock_completed_hour = self._create_mock_completed_hour(2, "Completed task")

        self._mock_db_responses(
            user=mock_user,
            bookings=[mock_booking],
            completed_hours=[mock_completed_hour],
        )

        result = generate_user_report(1, self.mock_db)
        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 1)
        self.assertTrue(output_df["Fecha"][0])  # Validación de campo nulo
        self.assertEqual(output_df["Horas Completadas"][0], 2)

    def test_generate_report_user_without_bookings_or_completed_hours(self):
        mock_user = self._create_mock_user()
        self._mock_db_responses(user=mock_user, bookings=[], completed_hours=[])

        result = generate_user_report(1, self.mock_db)
        self.assertIsInstance(result, BytesIO)

        output_df = pd.read_excel(result)
        self.assertEqual(len(output_df), 0)

    def test_generate_report_multiple_bookings_and_completed_hours(self):
        mock_user = self._create_mock_user()
        mock_booking1 = self._create_mock_booking(
            "2024-12-09", "Room A", "10:00", "11:00"
        )
        mock_booking2 = self._create_mock_booking(
            "2024-12-10", "Room B", "14:00", "15:00"
        )
        mock_completed_hour1 = self._create_mock_completed_hour(2, "Completed task 1")
        mock_completed_hour2 = self._create_mock_completed_hour(3, "Completed task 2")

        self._mock_db_responses(
            user=mock_user,
            bookings=[mock_booking1, mock_booking2],
            completed_hours=[mock_completed_hour1, mock_completed_hour2],
        )

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

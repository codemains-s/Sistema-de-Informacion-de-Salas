from sqlalchemy.orm import Session
from models.tables import User, CompletedHours, RoomBooking
import pandas as pd
from io import BytesIO
from openpyxl.styles import Alignment
from openpyxl.utils import get_column_letter


def generate_user_report(user_id: int, db: Session):
    # Obtener los datos del usuario
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        return None  # Retorna None si el usuario no existe

    # Obtener las reservas de salas del usuario
    room_bookings = db.query(RoomBooking).filter(RoomBooking.user_id == user_id).all()

    # Obtener las horas completadas del usuario
    completed_hours = (
        db.query(CompletedHours).filter(CompletedHours.user_id == user_id).all()
    )

    # Crear un DataFrame consolidado
    report_data = []
    accumulated_hours = 0
    for booking, hour in zip(room_bookings, completed_hours):
        completed = hour.hour_completed if hour else 0
        accumulated_hours += completed
        report_data.append(
            {
                "Fecha": booking.booking_date if booking else "N/A",
                "Sala": booking.room.name if booking else "N/A",
                "Hora Inicio": booking.start_time if booking else "N/A",
                "Hora Fin": booking.end_time if booking else "N/A",
                "Observaciones": hour.observations if hour else "N/A",
                "Nombre": user.name,
                "Horas Completadas": accumulated_hours,
            }
        )

    report_df = pd.DataFrame(report_data)

    # Generar el archivo Excel en memoria
    output = BytesIO()
    with pd.ExcelWriter(output, engine="openpyxl") as writer:
        report_df.to_excel(writer, index=False, sheet_name="User Report")

        # Aplicar estilos de centrado y ajustar el ancho
        workbook = writer.book
        worksheet = writer.sheets["User Report"]

        # Centrar el texto en todas las celdas
        for row in worksheet.iter_rows():
            for cell in row:
                cell.alignment = Alignment(horizontal="center", vertical="center")

        # Ajustar el ancho de las columnas
        for col_idx, col_cells in enumerate(worksheet.columns, start=1):
            max_length = (
                max(len(str(cell.value)) for cell in col_cells if cell.value) + 2
            )
            column_letter = get_column_letter(col_idx)
            worksheet.column_dimensions[column_letter].width = max_length

    output.seek(0)
    return output

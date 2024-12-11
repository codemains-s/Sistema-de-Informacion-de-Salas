from sqlalchemy import func, text
from schemas.room import Room
from models.tables import *
from typing import List, Optional

def create_room(new_room:Room, db):
    """
    Crea una nueva sala en la base de datos.

    Args:
        new_room (Room): Objeto Room con los datos de la nueva sala
        db: Sesión de la base de datos

    Returns:
        Room: El objeto Room creado y almacenado en la base de datos
    """
    db_room = Room(**new_room.dict())
    db.add(db_room)
    db.commit()
    db.refresh(db_room)
    return db_room

def all_rooms(db):
    """
    Obtiene todas las salas almacenadas en la base de datos.

    Args:
        db: Sesión de la base de datos

    Returns:
        list: Lista de todos los objetos Room en la base de datos
    """
    return db.query(Room).all()

def get_room_by_status(status: str, db):
    """
    Obtiene las salas que coinciden con el estado especificado.

    Args:
        status (str): Estado de la sala a consultar
        db: Sesión de la base de datos

    Returns:
        list: Lista de diccionarios con el nombre y estado de las salas que coinciden

    Raises:
        ValueError: Si no se proporciona un estado válido

    Example:
        >>> get_room_by_status("disponible", db)
        [
            {
                "name": "Sala 1",
                "status": "disponible"
            },
            {
                "name": "Sala 2", 
                "status": "disponible"
            }
        ]
    """
    if not status:
        raise ValueError("Se debe de ingresar un estado de sala a consultar")
    rooms = db.query(Room).all()

    rooms =  [
        {
            "name": room.name,
            "status": room.status,
        }
        for room in rooms if room.status == status
    ]
    return rooms

def get_room_by_id(id, db):
    """
    Obtiene una sala por su ID de la base de datos.

    Args:
        id (int): ID de la sala a buscar
        db: Sesión de la base de datos

    Returns:
        Room: Objeto Room encontrado o None si no existe
    """
    return db.query(Room).filter(Room.id == id).first()

def get_room_by_name(name: str, db):
    """
    Obtiene una sala por su nombre de la base de datos.

    Args:
        name (str): Nombre de la sala a buscar
        db: Sesión de la base de datos

    Returns:
        str: Mensaje indicando el resultado de la búsqueda
            - "Se debe de ingresar el nombre de la sala" si el nombre está vacío
            - "Sala encontrada" si se encuentra la sala
            - "Sala ingresada no existe" si no se encuentra la sala

    Note:
        La búsqueda es insensible a mayúsculas/minúsculas
    """
    if not name.strip():  # Verifica si el nombre está vacío o contiene solo espacios
        return "Se debe de ingresar el nombre de la sala"
    room = db.query(Room).filter(func.upper(Room.name) == name.upper()).first()
    if room:
        return "Sala encontrada"
    return "Sala ingresada no existe"

def update_room(id_room: int, updated_room: Room, db)->Optional[Room]:
    """
    Actualiza una sala existente en la base de datos.

    Args:
        id_room (int): ID de la sala a actualizar
        updated_room (Room): Objeto Room con los nuevos datos de la sala
        db: Sesión de la base de datos

    Returns:
        Optional[Room]: El objeto Room actualizado o None si no existe la sala

    Note:
        Esta función actualiza todos los campos de la sala:
        - Nombre
        - Dirección 
        - Descripción
        - Capacidad
        - Estado
        - Imagen
    """
    db_room = db.query(Room).filter(Room.id == id_room).first()
    db_room.name = updated_room.name
    db_room.address = updated_room.address
    db_room.description = updated_room.description
    db_room.capacity = updated_room.capacity
    db_room.status = updated_room.status
    db_room.image = updated_room.image
    db.commit()
    db.refresh(db_room)
    return db_room

def delete_room(id: int, db):
    db_room = db.query(Room).filter(Room.id == id).first()
    db.delete(db_room)
    db.commit()
    room_count = db.query(Room).count()
    if room_count == 0:
        db.execute(text("ALTER TABLE rooms AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Room.id)).scalar()
        db.execute(text(f"ALTER TABLE rooms AUTO_INCREMENT = {max_id + 1}"))
    db.commit()

    return db_room

def exist_room(name: str, db):
    return db.query(Room).filter(func.upper(Room.name) == name.upper()).first()
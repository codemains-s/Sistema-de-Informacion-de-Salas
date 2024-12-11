from schemas.role import Role
from models.tables import *
from sqlalchemy import func, text


def create_role(new_role: Role, db):
    """
    Crea un nuevo rol en la base de datos.

    Args:
        new_role (Role): Objeto Role con los datos del nuevo rol
        db: Sesión de la base de datos

    Returns:
        Role: El objeto Role creado y almacenado en la base de datos
    """
    db_role = Role(**new_role.dict())
    db.add(db_role)
    db.commit()
    db.refresh(db_role)
    return db_role

def all_roles(db):
    """
    Obtiene todos los roles almacenados en la base de datos.

    Args:
        db: Sesión de la base de datos

    Returns:
        list: Lista de todos los objetos Role en la base de datos
    """
    return db.query(Role).all()

def get_role_by_id(id: int, db):
    """
    Obtiene un rol por su ID de la base de datos.

    Args:
        id (int): ID del rol a buscar
        db: Sesión de la base de datos

    Returns:
        Role: Objeto Role encontrado o None si no existe
    """
    return db.query(Role).filter(Role.id == id).first()

def get_role_by_name(name:str, db):
    """
    Obtiene el ID de un rol por su nombre de la base de datos.

    Args:
        name (str): Nombre del rol a buscar
        db: Sesión de la base de datos

    Returns:
        int: ID del rol encontrado

    Raises:
        AttributeError: Si no se encuentra el rol con el nombre especificado
    """
    rol = db.query(Role).filter(Role.name == name).first()
    return rol.id

def update_role(id_role, updated_role: Role, db):
    """
    Actualiza un rol existente en la base de datos.

    Args:
        id_role (int): ID del rol a actualizar
        updated_role (Role): Objeto Role con los nuevos datos del rol
        db: Sesión de la base de datos

    Returns:
        Role: El objeto Role actualizado

    """
    db_role = db.query(Role).filter(Role.id == id_role).first()
    db_role.name = updated_role.name
    db.commit()
    db.refresh(db_role)
    return db_role

def delete_role(id: int, db):
    """
    Elimina un rol de la base de datos y reajusta el auto-incremento.

    Args:
        id (int): ID del rol a eliminar
        db: Sesión de la base de datos

    Returns:
        Role: El objeto Role eliminado

    Note:
        Esta función también maneja el reajuste del auto-incremento de la tabla.
        Si no quedan registros, reinicia el auto-incremento a 1.
        Si quedan registros, establece el auto-incremento al siguiente valor después del ID máximo.
    """
    db_role = db.query(Role).filter(Role.id == id).first()
    db.delete(db_role)
    db.commit()
    role_count = db.query(Role).count()
    if role_count == 0:
        db.execute(text("ALTER TABLE roles AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Role.id)).scalar()
        db.execute(text(f"ALTER TABLE roles AUTO_INCREMENT = {max_id + 1}"))
    db.commit()
    return db_role

def exist_role(name:str, db):
    """
    Verifica si existe un rol con el nombre especificado.

    Args:
        name (str): Nombre del rol a buscar
        db: Sesión de la base de datos

    Returns:
        Role: Objeto Role si existe, None si no existe
    """
    role = db.query(Role).filter(func.upper(Role.name) == name.upper()).first()
    return role

def exist_role_id(id: int, db):
    """
    Verifica si existe un rol con el ID especificado.

    Args:
        id (int): ID del rol a buscar
        db: Sesión de la base de datos

    Returns:
        Role: Objeto Role si existe, None si no existe
    """
    role = db.query(Role).filter(Role.id == id).first()
    return role
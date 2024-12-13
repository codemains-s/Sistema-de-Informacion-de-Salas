from schemas.program import Program
from models.tables import *
from sqlalchemy import func, text

def create_program(new_program: Program, db):
    """
    Crea un nuevo programa en la base de datos.

    Args:
        new_program (Program): Objeto Program con los datos del nuevo programa
        db: Sesión de la base de datos

    Returns:
        Program: El objeto Program creado y almacenado en la base de datos

    """
    db_program = Program(**new_program.dict())
    db.add(db_program)
    db.commit()
    db.refresh(db_program)
    return db_program

def all_programs(db):
    """
    Obtiene todos los programas almacenados en la base de datos.

    Args:
        db: Sesión de la base de datos

    Returns:
        list: Lista de todos los objetos Program en la base de datos
    """
    return db.query(Program).all()

def get_program_by_id(id: int, db):
    """
    Obtiene un programa por su ID de la base de datos.

    Args:
        id (int): ID del programa a buscar
        db: Sesión de la base de datos

    Returns:
        Program: Objeto Program encontrado o None si no existe
    """
    return db.query(Program).filter(Program.id == id).first()

def get_id_program_by_name(name: str, db):
    """
    Obtiene el ID de un programa por su nombre de la base de datos.

    Args:
        name (str): Nombre del programa a buscar
        db: Sesión de la base de datos

    Returns:
        int: ID del programa encontrado

    Raises:
        AttributeError: Si no se encuentra el programa con el nombre especificado
    """
    program = db.query(Program).filter(func.upper(Program.name) == name.upper()).first()
    return program.id

def update_program(id: int, new_program: Program, db):
    """
    Actualiza un programa existente en la base de datos.

    Args:
        id (int): ID del programa a actualizar
        new_program (Program): Objeto Program con los nuevos datos del programa
        db: Sesión de la base de datos

    Returns:
        Program: El objeto Program actualizado

    """
    db_program = db.query(Program).filter(Program.id == id).first()
    db_program.name = new_program.name
    db_program.description = new_program.description
    db.commit()
    db.refresh(db_program)
    return db_program

def delete_program(id: int, db):
    """
    Elimina un programa de la base de datos y reajusta el auto-incremento.

    Args:
        id (int): ID del programa a eliminar
        db: Sesión de la base de datos

    Returns:
        Program: El objeto Program eliminado

    Note:
        Esta función también maneja el reajuste del auto-incremento de la tabla.
        Si no quedan registros, reinicia el auto-incremento a 1.
        Si quedan registros, establece el auto-incremento al siguiente valor después del ID máximo.
    """
    program = db.query(Program).filter(Program.id == id).first()
    db.delete(program)
    db.commit()
    count = db.query(Program).count()
    if count == 0:
        db.execute(text("ALTER TABLE program AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Program.id)).scalar()
        db.execute(text(f"ALTER TABLE program AUTO_INCREMENT = {max_id + 1}"))
    db.commit()
    return program

def exist_program(id: int, db):
    """
    Verifica si existe un programa con el ID especificado.

    Args:
        id (int): ID del programa a buscar
        db: Sesión de la base de datos

    Returns:
        Program: Objeto Program si existe, None si no existe
    """
    return db.query(Program).filter(Program.id == id).first()

def exist_program_by_name(name: str, db):
    """
    Verifica si existe un programa con el nombre especificado.

    Args:
        name (str): Nombre del programa a buscar
        db: Sesión de la base de datos

    Returns:
        Program: Objeto Program si existe, None si no existe
    """
    return db.query(Program).filter(Program.name == name).first()
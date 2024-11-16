from schemas.program import Program
from models.tables import *
from sqlalchemy import func, text

def create_program(new_program: Program, db):
    db_program = Program(**new_program.dict())
    db.add(db_program)
    db.commit()
    db.refresh(db_program)
    return db_program

def all_programs(db):
    return db.query(Program).all()

def get_program_by_id(id: int, db):
    return db.query(Program).filter(Program.id == id).first()

def update_program(id: int, new_program: Program, db):
    db_program = db.query(Program).filter(Program.id == id).first()
    db_program.name = new_program.name
    db_program.description = new_program.description
    db.commit()
    db.refresh(db_program)
    return db_program

def delete_program(id: int, db):
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
    return db.query(Program).filter(Program.id == id).first()

def exist_program_by_name(name: str, db):
    return db.query(Program).filter(Program.name == name).first()
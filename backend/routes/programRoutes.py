from fastapi import APIRouter, Depends, HTTPException, Request, status
from schemas.program import Program
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.programController import (create_program, all_programs, 
                                           get_program_by_id, update_program, 
                                           delete_program, exist_program, exist_program_by_name, get_id_program_by_name)

router = APIRouter()

@router.post("/new_program/")
def new_program(program: Program, db: Session = Depends(get_db)):
    exist = exist_program_by_name(program.name, db)
    if exist:
        raise HTTPException(status_code=400, detail="The program already exists")
    program = create_program(program, db)
    return program

@router.get("/all_programs/")
def programs(db: Session = Depends(get_db)):
    return all_programs(db)

@router.get("/program_id/{name}", dependencies=[Depends(Portador())])
def program_id(name: str, db: Session = Depends(get_db)):
    exist = exist_program_by_name(name, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The program does not exist")
    program_id = get_id_program_by_name(name, db)
    return program_id

@router.get("/program/{id}", dependencies=[Depends(Portador())])
def program(id: int, db: Session = Depends(get_db)):
    exist = exist_program(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The program does not exist")
    program = get_program_by_id(id, db)
    return program

@router.put("/update_program/{id}", dependencies=[Depends(Portador())])
def update_program_by_id(id: int, program: Program, db: Session = Depends(get_db)):
    exist = exist_program(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The program does not exist")
    program = update_program(id, program, db)
    return program

@router.delete("/delete_program/{id}", dependencies=[Depends(Portador())])
def delete_program_by_id(id: int, db: Session = Depends(get_db)):
    exist = exist_program(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The program does not exist")
    program = delete_program(id, db)
    return program
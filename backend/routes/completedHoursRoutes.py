from fastapi import APIRouter, Depends, HTTPException, Request, status
from schemas.completedHour import CompletedHour, CompletedHourOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.userController import exist_user_id
from controllers.completedHourController import (create_completed_hour, all_completed_hours, 
                                                 get_completed_hour_by_id, get_completed_hour_by_user_id, 
                                                 update_completed_hour, delete_completed_hour, exist_completed_hour)

router = APIRouter()

# Create completed hour

@router.post("/new_completed_hour/", dependencies=[Depends(Portador())])
def create_new_completed_hour(completed_hour: CompletedHour, db: Session = Depends(get_db)):
    new_completed_hour = create_completed_hour(completed_hour, db)
    return CompletedHour(**new_completed_hour.__dict__)

@router.get("/completedHours/", dependencies=[Depends(Portador())])
def get_all_completed_hours_route(db: Session = Depends(get_db)):
    return all_completed_hours(db)

@router.get("/completedHour/{id}", dependencies=[Depends(Portador())])
def get_completed_hour(id: int, db: Session = Depends(get_db)):
    completed_hour = get_completed_hour_by_id(id, db)
    exist = exist_completed_hour(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The completed hour does not exist")
    
    return CompletedHourOut(**completed_hour.__dict__)

@router.get("/completedHoursUser/{user_id}", dependencies=[Depends(Portador())])
def get_completed_hour_by_user_id_route(user_id: int, db: Session = Depends(get_db)):
    exist = exist_user_id(user_id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The user does not exist")
    hour = get_completed_hour_by_user_id(user_id, db)
    return CompletedHourOut(**hour.__dict__)

@router.put("/update_completed_hour/{id}", dependencies=[Depends(Portador())])
def update_completed_hour_route(id: int, completed_hour: CompletedHour, db: Session = Depends(get_db)):
    exist = exist_completed_hour(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The completed hour does not exist")
    updated_completed_hour = update_completed_hour(id, completed_hour, db)
    return CompletedHourOut(**updated_completed_hour.__dict__)

@router.delete("/delete_completed_hour/{id}", dependencies=[Depends(Portador())])
def delete_completed_hour_route(id: int, db: Session = Depends(get_db)):
    exist = exist_completed_hour(id, db)
    if not exist:
        raise HTTPException(status_code=404, detail="The completed hour does not exist")
    delete_completed_hour(id, db)
    return {"message": "The completed hour has been deleted"}

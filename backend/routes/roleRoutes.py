from fastapi import APIRouter, Depends
from schemas.role import Role, RoleOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.roleController import all_roles, create_role, update_role, delete_role, exist_role

router = APIRouter()

# Create role

@router.post("/new_role/")
def create_new_role(role: Role, db: Session = Depends(get_db)):
    exist = exist_role(role.name, db)
    if exist:
        return {"error": "Role already exists"}
    new_rolee = create_role(role, db)
    return Role(**new_rolee.__dict__)

# get role by name

@router.get("/role/{name}", dependencies=[Depends(Portador())])
def get_role(name: str, db: Session = Depends(get_db)):
    exist = exist_role(name, db)
    if not exist:
        return {"message": "Role not exist"}
    return RoleOut(**exist.__dict__)

# get all roles

@router.get("/all_roles", dependencies=[Depends(Portador())])
def get_all_roles(db: Session = Depends(get_db)):
    return all_roles(db)

# delete role

@router.delete("/delete_role/{role_id}", dependencies=[Depends(Portador())])
def delete_roles(role_id: int, db: Session = Depends(get_db)):
    role_deleted = delete_role(role_id, db)
    if not role_deleted:
        return {"message": "Role not exist"}
    return {"message": "Role deleted successfully"}


# update role

@router.put("/update_role/{role_id}", dependencies=[Depends(Portador())])
def update_role_by_id(role_id: int, role: Role, db: Session = Depends(get_db)):
    role_updated = update_role(role_id, role, db)
    if not role_updated:
        return {"message": "Role not exist"}
    return role_updated






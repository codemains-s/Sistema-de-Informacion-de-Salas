from fastapi import APIRouter, Depends
from schemas.user import User, UserOut
from config.db import get_db
from sqlalchemy.orm import Session
from controllers.userController import create_user, get_all_users, update_user, delete_user, exist_user

router = APIRouter()

# Create a new user
@router.post("/new_user/")
def new_user(user: User, db: Session = Depends(get_db)):
    exist = exist_user(user.name, db)
    if exist:
        return {"error": "User already exists"}
    new_userr = create_user(user, db)
    return User(**new_userr.__dict__)

# get user by name
@router.get("/user/{name}")
def get_user(name: str, db: Session = Depends(get_db)):
    exist = exist_user(name, db)
    if not exist:
        return {"message": "author not exist"}
    return UserOut(**exist.__dict__)

# get all users
@router.get("/all_users/")
def all_users(db: Session = Depends(get_db)):
    return get_all_users(db)

# delete user
@router.delete("/delete_user/{user_id}")
def delete_users(user_id: int, db: Session = Depends(get_db)):
    user_deleted = delete_user(user_id, db)
    if not user_deleted:
        return {"message": "User not exist"}
    return {"message": "User deleted successfully"}

# update user
@router.put("/update_user/{user_id}")
def update_user_by_id(user_id: int, user: User, db: Session = Depends(get_db)):
    user_updated = update_user(user_id, user, db)
    if not user_updated:
        return {"message": "User not exist"}
    return user_updated


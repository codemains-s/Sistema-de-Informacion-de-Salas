from fastapi import APIRouter, Depends, HTTPException, Request, status
from schemas.user import User, UserCreate, UserOut, UserUpdate, UserLogin, UserEdit
from config.db import get_db
from sqlalchemy.orm import Session
from controllers.emailController import send_welcome_email
from security.seguridad import Portador
from controllers.userController import (create_user, get_all_users, 
                                        update_user, delete_user, exist_user, 
                                        email_validation, validate_password, 
                                        exist_token, password_context)


router = APIRouter()

# Create a new user
@router.post("/register_user/")
async def register(new_user: UserCreate, db: Session=Depends(get_db), code: str = ""):
    email_validation(new_user.email)
    validate_password(new_user.password)
    if exist_user(new_user.email, db):
        raise HTTPException(status_code=400, detail="User already exist")
    if not email_validation(new_user.email):
        raise HTTPException(status_code=400, detail="Email not valid")
    if not validate_password(new_user.password):
        raise HTTPException(status_code=400, detail="Password not valid")
    await send_welcome_email(new_user.email, new_user.name, Request)
    token = create_user(new_user, code, db)
    return {"message": "User created successfully"}

# Login

@router.post("/login_user/")
def login(request: UserLogin, db: Session = Depends(get_db)):
    email_validation(request.email)
    usr = exist_user(request.email, db)
    if not usr:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not exist")
    if not password_context.verify(request.password, usr.password):
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Password incorrect")
    token = exist_token(request.email, request.password, db)
    if not token:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Token not exist")
    
    return {"Token": token}



# get user by name
"""@router.get("/user/{email}", dependencies=[Depends(Portador())])
def get_user(email: str, db: Session = Depends(get_db)):
    exist = exist_user(email, db)
    if not exist:
        return {"message": "user not exist"}
    return UserOut(**exist.__dict__) """

# get all users
@router.get("/all_users/", dependencies=[Depends(Portador())])
def all_users(db: Session = Depends(get_db)):
    return get_all_users(db)

# delete user
@router.delete("/delete_user/{user_id}", dependencies=[Depends(Portador())])
def delete_users(user_id: int, db: Session = Depends(get_db)):
    user_deleted = delete_user(user_id, db)
    if not user_deleted:
        return {"message": "User not exist"}
    return {"message": "User deleted successfully"}

# update user
@router.put("/update_user/{user_id}", dependencies=[Depends(Portador())])
def update_user_by_id(user_id: int, user: UserEdit, db: Session = Depends(get_db)):
    user_updated = update_user(user_id, user, db)
    if not user_updated:
        return {"message": "User not exist"}
    return user_updated

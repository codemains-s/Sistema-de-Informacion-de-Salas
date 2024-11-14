from fastapi.security import HTTPBearer
from fastapi import Depends, Request, HTTPException, status
from controllers.userController import validate_token
from controllers.roleController import get_role_by_name
from config.db import get_db


class Portador(HTTPBearer):
    async def __call__(self, request: Request, db=Depends(get_db)):
        try:
            # mostrar la cabecera de autorización
            print(request.headers.get("Authorization"))

            token = await super().__call__(request)
            datos = validate_token(token.credentials)
            role = datos.get("role_id")

            if role is None or role != get_role_by_name("Coordinator", db):
                raise HTTPException(
                    status_code=status.HTTP_403_FORBIDDEN, detail="Access denied"
                )
            return token
        except HTTPException as e:
            raise e
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="No cuentas con los permisos necesarios para esta operación",
            ) from e

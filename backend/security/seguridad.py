from fastapi.security import HTTPBearer
from fastapi import Depends, Request, HTTPException, status
from controllers.userController import validate_token
from controllers.roleController import get_role_by_name
from config.db import get_db


class Portador(HTTPBearer):
    async def __call__(self, request: Request, db=Depends(get_db)):
        try:
            # Mostrar la cabecera de autorización
            print(request.headers.get("Authorization"))

            token = await super().__call__(request)
            print(token)
            datos = validate_token(token.credentials)
            role = datos.get("role_id")

            # Verificamos el rol del usuario
            if role is None or role != get_role_by_name("Coordinator", db):
                # Si el rol no es 'Coordinator', lanzamos un 403 con un mensaje claro
                raise HTTPException(
                    status_code=status.HTTP_403_FORBIDDEN, 
                    detail="No cuentas con permisos para acceder a este recurso"
                )
            return token
        except HTTPException as e:
            raise e
        except Exception as e:
            # En caso de un error inesperado, lanzamos un 401 con un mensaje adecuado
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="No cuentas con los permisos necesarios para esta operación",
            ) from e

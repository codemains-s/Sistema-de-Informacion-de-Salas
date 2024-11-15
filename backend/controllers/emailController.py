import os
import locale
from models.tables import *
from dotenv import load_dotenv
from fastapi_mail import FastMail, MessageSchema, ConnectionConfig
from fastapi import Request, HTTPException, status
from schemas.user import User
from starlette.responses import JSONResponse
from typing import Optional


locale.setlocale(locale.LC_TIME, 'es_ES')
load_dotenv('.env')

smtp_password = os.getenv("SMTP_PASSWORD")
smpt_from = os.getenv("SMTP_FROM")

conf = ConnectionConfig(
    MAIL_USERNAME=smpt_from,
    MAIL_PASSWORD=smtp_password,
    MAIL_FROM=smpt_from,
    MAIL_PORT=587,
    MAIL_SERVER="smtp.gmail.com",
    MAIL_FROM_NAME="SIS",
    MAIL_STARTTLS=True,
    MAIL_SSL_TLS=False,
)
async def send_welcome_email(email: str, username: str, request: Request):
    template = f"""
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="utf-8">
            <title>¡Bienvenido a SIS! Registro exitoso</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
            <style>
                body {{
                    font-family: 'Arial', sans-serif;
                    background: linear-gradient(to right, #4CAF50, #8BC34A);
                    margin: 0;
                    padding: 0;
                }}
                .container {{
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                }}
                .container h2 {{
                    color: #ffffff;
                    margin: 0;
                    border-radius: 10px 10px 0 0;
                    background-color: #3f51b5;
                    padding: 15px;
                    text-align: center;
                }}
                .container p {{
                    color: #333;
                    line-height: 1.6;
                }}
                .container strong {{
                    color: #3f51b5;
                    font-weight: bold;
                }}
                .btn {{
                    display: inline-block;
                    padding: 10px 20px;
                    text-align: center;
                    background-color: #FF5722;
                    color: #fff;
                    text-decoration: none;
                    border-radius: 5px;
                    margin-top: 15px;
                    transition: background-color 0.3s ease;
                }}
                .btn:hover {{
                    background-color: #E64A19;
                }}
                .footer {{
                    margin-top: 20px;
                    text-align: center;
                    color: #777;
                }}
                .conten {{
                    margin: 0 20px;
                }}
            </style>
        </head>
        <body>
            <div class="container">
                <h2>Registro exitoso en SIS</h2>
                <div class="conten">
                    <p>¡Hola <strong>{username}</strong>! Te damos la bienvenida a SIS, el Sistema de Información de Salas para Monitores. Estamos emocionados de tenerte en nuestra comunidad.</p>      
                    <p>A través de nuestra aplicación, podrás gestionar de manera eficiente las salas disponibles, coordinar con otros monitores y asegurarte de que todos los recursos estén bien organizados.</p>
                    <p>¡Disfruta explorando nuestra app y no dudes en ponerte en contacto si necesitas ayuda!</p>
                    <a href="#" class="btn">Explorar la aplicación</a>
                </div>
            </div>
            <div class="footer">
                <p>© 2024 SIS | Todos los derechos reservados</p>
            </div>
        </body>
        </html>"""

    message = MessageSchema(
        subject=f"Hola {username}, Bienvenido a SIS",
        recipients=[email],
        body=template,
        subtype="html",
    )
    # Enviar mensaje
    fm = FastMail(conf)
    try:
        await fm.send_message(message=message)
        return JSONResponse(status_code=200, content={"message": "El correo electrónico ha sido enviado"})
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"Error al enviar el correo electrónico: {str(e)}"
        )


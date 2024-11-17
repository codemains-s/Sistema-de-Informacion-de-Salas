from fastapi.testclient import TestClient
from sqlalchemy.orm import Session
from main import app
from config.db import get_db
from security.seguridad import Portador

client = TestClient(app)


VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6bnVsbCwiZW1haWwiOiJqcC5hbGV4YW5kZXJvZmYxQGdtYWlsLmNvbSIsInJvbGVfaWQiOjJ9.dmfscHiiUVjNrxiM7lbACwPnMBhIKwNgH294WSQa94o"


# Mocks
class FakeDBSession:
    def query(self, model):
        return self

    def all(self):
        # Simula una lista de habitaciones
        return [{"id": 1, "name": "Room A"}, {"id": 2, "name": "Room B"}]

    def filter(self, condition):
        # Simula la obtención de una habitación por ID
        if condition.left.value == 1:
            return [FakeRoom(id=1, name="Room A")]
        return []


class FakeRoom:
    def __init__(self, id, name):
        self.id = id
        self.name = name

    def __dict__(self):
        return {"id": self.id, "name": self.name}


# Sobrescribe las dependencias para pruebas
def fake_get_db():
    return FakeDBSession()


app.dependency_overrides[get_db] = fake_get_db
app.dependency_overrides[Portador] = (
    lambda: None
)  # Mocks 'Portador' para omitir la autenticación


# Pruebas
def test_get_all_rooms():
    response = client.get("/all_rooms", headers={"Authorization": VALID_TOKEN})
    assert response.status_code == 200


def CS_01():
    response = client.get("/room_by_id?id=1", headers={"Authorization": VALID_TOKEN})
    assert response.status_code == 200


def CS_02():
    response = client.get(
        "/room_by_name?name=salajota", headers={"Authorization": VALID_TOKEN}
    )
    assert response.status_code == 200 or response.status_code == 404


# nombre de sala vacío o con espacios en blanco
def CS_03():
    response = client.get("/room_by_name?name=", headers={"Authorization": VALID_TOKEN})
    assert response.status_code == 200 or response.status_code == 404


# nombre con mayúsculas
def CS_04():
    response = client.get(
        "/room_by_name?name=SALA J", headers={"Authorization": VALID_TOKEN}
    )
    assert response.status_code == 200


# dato distinto a una cadena de texto
def CS_05():
    response = client.get(
        "/room_by_name?name=123", headers={"Authorization": VALID_TOKEN}
    )
    assert response.status_code == 422

import {useNavigate, useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import Input from "../components/Input.jsx";
import AuthContext from "../context/AuthContext.jsx";
import Select from "../components/Select.jsx";

const EditEventPage = () => {

    const {setUser} = useContext(AuthContext);
    const navigate = useNavigate();
    const {id} = useParams();

    const [name, setName] = useState("");
    const [capacity, setCapacity] = useState(1);
    const [visibility, setVisibility] = useState("PUBLIC");

    useEffect(() => {
        const fetchEvent = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/events/${id}`, {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if(response.status === 401) {
                setUser(null);
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const event = await response.json();
            setName(event.name);
            setCapacity(Number(event.capacity));
            setVisibility(event.visibility);
        }

        fetchEvent();
    }, [id]);

    const handleChange = async (event) => {
        event.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events/${id}`, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({name, capacity, visibility})
        });

        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        console.log(200);
        navigate(`/events/${id}`);
    }

    return (
        <div>
            <form onSubmit={handleChange}>
                <Input value={name} id="name" onChange={(e) => setName(e.target.value)}>Название</Input>
                <Input value={capacity} id="capacity" type='number' onChange={(e) => setCapacity(e.target.value)}>Максимальное количество участников</Input>
                <Select
                    id="vsibility"
                    value={visibility}
                    options={[
                        {label:'Публичный', value:'PUBLIC'},
                        {label:'Приватный', value:'PRIVATE'}
                    ]}
                    onChange={(e) => setVisibility(e.target.value)}
                >Видимость</Select>
                <button>Изменить</button>
            </form>
        </div>
    )
}

export default EditEventPage;
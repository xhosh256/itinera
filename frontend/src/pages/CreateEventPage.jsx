import Header from "../components/Header.jsx";
import Input from "../components/Input.jsx";
import Select from "../components/Select.jsx";
import {useContext, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import LoginPage from "./LoginPage.jsx";
import {Link, useNavigate} from "react-router-dom";

const CreateEventPage = () => {

    const { user, setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const [name, setName] = useState("");
    const [capacity, setCapacity] = useState(1);
    const [visibility, setVisibility] = useState("PUBLIC");

    if (user===null) {
        return <LoginPage />
    }

    const handleCreate = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name, capacity, visibility})
        })

        if (response.status === 401) {
            setUser(null);
            return <LoginPage />
        }

        if(!response.ok){
            throw new Error(response.statusText);
        }

        const event = await response.json();

        navigate(`/events/${event.id}`);
    }

    return (
        <>
            <Header />

            <form className="create__event__form" onSubmit={handleCreate}>
                <Input
                    id="event-name"
                    type="text"
                    onChange={(e) => setName(e.target.value)}
                >Название ивента</Input>
                <Input
                    id="event-capacity"
                    type='number'
                    onChange={(e) => setCapacity(Number(e.target.value))}
                >Максимальное количетво человек</Input>
                <Select
                    id="vsibility"
                    value={visibility}
                    options={[
                        {label:'Публичный', value:'PUBLIC'},
                        {label:'Приватный', value:'PRIVATE'}
                    ]}
                    onChange={(e) => setVisibility(e.target.value)}
                >Видимость</Select>

                <button type='submit'>
                    Создать
                </button>
            </form>
        </>
    )
};

export default CreateEventPage;
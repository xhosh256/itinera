import Header from "../components/Header.jsx";
import Input from "../components/Input.jsx";
import {useContext, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import AuthContext from "../context/AuthContext.jsx";

const CreateComponentPage = () => {

    const {id} = useParams();
    const {setUser} = useContext(AuthContext);
    const navigate = useNavigate();

    const [startTime, setStartTime] = useState(new Date());
    const [endTime, setEndTime] = useState(new Date());
    const [description, setDescription] = useState("");
    const [address, setAddress] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events/${id}/components`, {
            credentials: "include",
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({startTime, endTime, description, address}),
        });

        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }


        navigate(`/events/${id}`);
    }

    return (
        <div>
            <Header />

            <form onSubmit={handleSubmit}>

                <Input
                    id="startTime"
                    type="datetime-local"
                    value={startTime}
                    onChange={(e) => setStartTime(e.target.value)}
                >Время начала</Input>

                <Input
                    id="endTime"
                    type="datetime-local"
                    value={endTime}
                    onChange={(e) => setEndTime(e.target.value)}
                >Время конца</Input>

                <Input
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                >Описание компонента</Input>

                <Input
                    id="address"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                >Адрес</Input>

                <button>Добавить</button>
            </form>
        </div>
    )
}

export default CreateComponentPage;
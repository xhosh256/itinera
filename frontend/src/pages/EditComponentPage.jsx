import {useNavigate, useParams} from "react-router-dom";
import Input from "../components/Input.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";

const EditComponentPage = () => {
    const {eventId, componentId} = useParams();
    const {user, setUser} = useContext(AuthContext);
    const navigate = useNavigate();

    const [startTime, setStartTime] = useState(new Date());
    const [endTime, setEndTime] = useState(new Date());
    const [description, setDescription] = useState("");
    const [address, setAddress] = useState("");

    useEffect(() => {
        const fetchComponent = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/events/${eventId}/components/${componentId}`, {
                credentials: "include",
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            if(response.status === 401) {
                setUser(null);
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText)
            }

            const component = await response.json();
            setStartTime(component.startTime);
            setEndTime(component.endTime);
            setDescription(component.description);
            setAddress(component.address);
        }

        fetchComponent();
    }, [eventId, componentId]);

    const handleEdit = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events/${eventId}/components/${componentId}`, {
            credentials: "include",
            method: "PUT",
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
            throw new Error(response.statusText)
        }

        navigate(`/events/${eventId}`);
    }

    return (
        <div>
            <form onSubmit={handleEdit}>
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

                <button type="submit">Изменить</button>
            </form>
        </div>
    );
}

export default EditComponentPage;
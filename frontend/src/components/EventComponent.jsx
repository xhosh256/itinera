import './EventComponent.css'
import {Link, useNavigate} from "react-router-dom";
import {useContext} from "react";
import AuthContext from "../context/AuthContext.jsx";

const EventComponent = (props) => {
    const {
        componentId,
        event,
        startTime,
        endTime,
        description,
        address,
        onDeleted
    } = props;

    const start = new Date(startTime);
    const end = new Date(endTime);

    const navigate = useNavigate();

    const {user, setUser} = useContext(AuthContext);

    const handleDelete = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/events/${event.id}/components/${componentId}`, {
            credentials: "include",
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if(response.status === 401) {
            setUser(null);
            return
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        onDeleted(componentId);
    }

    return (
        <div className="event__component">
            <p>{start.toLocaleString('ru-RU', {
                day: '2-digit',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })} — {end.toLocaleString('ru-RU', {
                day: '2-digit',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })}</p>

            <p>О событии: {description}</p>
            <p>Место: {address}</p>

            {user.id === event.hostId && (
                <>
                    <Link to={`/events/${event.id}/components/${componentId}/edit`}>Редактировать Компонент</Link>
                    <button onClick={handleDelete}>Удалить Компонент</button>
                </>
            )}
        </div>
    )
}

export default EventComponent;
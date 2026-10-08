import {Link} from "react-router-dom";
import EventPreview from "../components/EventPreview.jsx";
import Header from "../components/Header.jsx";
import LoginPage from "./LoginPage.jsx";
import {useContext} from "react";
import AuthContext from "../context/AuthContext.jsx";

const MyEventsPage = () => {
    const { user } = useContext(AuthContext);

    if (user === null) return <LoginPage />;

    const myEvents = [
        {id: 1, name: "hueta", capacity: 12, participantIds: [1, 2, 3,4, 5]},
    ];

    if(myEvents.length === 0) return (
        <div>
            <Header />
            <h1>Создайте свое первое событие прямо сейчас!</h1>
            <Link to="/events/create">Создать Событие</Link>
        </div>
    );

    return (
        <div>
            <Header />
            {myEvents.map(({id, name, capacity, participantIds}) => (
                <EventPreview
                    key={id}
                    id={id}
                    name={name}
                    capacity={capacity}
                    participantIds={participantIds}
                />
            ))}
        </div>
    );
}

export default MyEventsPage;
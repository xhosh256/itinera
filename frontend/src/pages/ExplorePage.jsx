import Header from "../components/Header.jsx";
import {Link} from "react-router-dom";
import EventPreview from "../components/EventPreview.jsx";

const ExplorePage = () => {

    const upcomingEvents = [
        {id: 1, name: "event 1", capacity: 200, participantIds: [13213, 313131 ,1231231 ,321]},
        {id: 2, name: "some cool event", capacity: 3, participantIds: [67]},
        {id: 3, name: "idk", capacity: 20, participantIds: [1, 2, 3, 4, 5]},
    ]

    return (
        <>
            <div>
                <h1>Upcoming Events</h1>
                {upcomingEvents.map(({id, name, capacity, participantIds}) => (
                    <EventPreview
                        key={id}
                        id={id}
                        name={name}
                        capacity={capacity}
                        participantIds={participantIds}
                    />
                ))}
            </div>
            <div>
                <h1>Создайте свое первое событие прямо сейчас и приглашайте остальных посетить его!</h1>
                <Link to="/events">Создать Событие</Link>
            </div>
        </>
    )
}

export default ExplorePage
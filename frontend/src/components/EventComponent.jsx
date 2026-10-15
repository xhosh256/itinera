import './EventComponent.css'

const EventComponent = (props) => {
    const {
        startTime,
        endTime,
        description,
        address
    } = props;

    const start = new Date(startTime);
    const end = new Date(endTime);

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
        </div>
    )
}

export default EventComponent;
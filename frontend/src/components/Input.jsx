const Input = (props) => {
    const {
        id,
        label,
        type='text',
        onChange,
    } = props

    return (
        <>
            <label htmlFor={id}> {label} </label>
            <input type={type} id={id} onChange={onChange}/>
        </>
    )
}

export default Input;
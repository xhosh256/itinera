const Input = (props) => {
    const {
        id,
        children,
        type='text',
        onChange,
    } = props

    return (
        <>
            <label htmlFor={id}> {children} </label>
            <input type={type} id={id} onChange={onChange}/>
        </>
    )
}

export default Input;
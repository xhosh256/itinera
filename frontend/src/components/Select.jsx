const Select = (props) => {

    const {
        id,
        children,
        value,
        options,
        onChange,
    } = props

    return (
        
        <>
            <label htmlFor="{id}">{children}</label>
            
            <select id={id} value={value} onChange={onChange}>
                {options.map(option => (
                    <option key={option.value} value={option.value}>{option.label}</option>
                ))}
            </select>
        </>
        
    )
}

export default Select;
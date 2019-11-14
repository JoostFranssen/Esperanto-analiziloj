function snakeCaseToTitleCase(string) {
    let parts = string.toLowerCase().split("_");
    for(let i = 0; i < parts.length; i++) {
        parts[i] = parts[i].charAt(0).toUpperCase() + parts[i].substring(1);
    }
    return parts.join(" ");
}

// ========================================

ReactDOM.render(
    <Main />,
    document.getElementById('root')
);
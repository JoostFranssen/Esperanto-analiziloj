function snakeCaseToTitleCase(string) {
    let parts = string.toLowerCase().split("_");
    for(let i = 0; i < parts.length; i++) {
        parts[i] = parts[i].charAt(0).toUpperCase() + parts[i].substring(1);
    }
    return parts.join(" ");
}

var map = {
    c: "ĉ",
    C: "Ĉ",
    g: "ĝ",
    G: "Ĝ",
    h: "ĥ",
    H: "Ĥ",
    j: "ĵ",
    J: "Ĵ",
    s: "ŝ",
    S: "Ŝ",
    u: "ŭ",
    U: "Ŭ",
}

// ========================================

ReactDOM.render(
    <Main />,
    document.getElementById('root')
);
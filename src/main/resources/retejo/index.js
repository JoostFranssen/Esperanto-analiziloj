function snakeCaseToTitleCase(string) {
    let parts = string.toLowerCase().split("_");
    for(let i = 0; i < parts.length; i++) {
        parts[i] = parts[i].charAt(0).toUpperCase() + parts[i].substring(1);
    }
    return parts.join(" ");
}

function convertXSystem(string) {
    let map = {
        cx: "ĉ",
        gx: "ĝ",
        hx: "ĥ",
        jx: "ĵ",
        sx: "ŝ",
        ux: "ŭ",
    }
    let regex = new RegExp(Object.keys(map).join("|"), "gi");
    return string.replace(regex, matched => {
        let firstChar = matched.charAt(0);
        let isUpper = (firstChar === firstChar.toUpperCase());
        matched = matched.toLowerCase();
        let result = map[matched];
        return isUpper ? result.toUpperCase() : result;
    });
}

// ========================================

ReactDOM.render(
    <Main />,
    document.getElementById('root')
);
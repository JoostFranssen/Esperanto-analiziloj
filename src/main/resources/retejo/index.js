class VortAnalizilo extends React.Component {
    render() {
        return (
            <form onSubmit={(event) => this.handleSubmit(event, document.getElementById("text-input").value)}>
                <div id="input-container">
                    <label>
                        Analizota vorto:
                    </label>
                    <input
                        type="text"
                        name="vorto"
                        id="text-input"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        required
                    />
                </div>
                <input type="submit" value="Analizi"/>
            </form>
        );
    }

    async handleSubmit(event, value) {
        event.preventDefault();
        let response = await fetch(`api/vortanalizo?vorto=${value}`);
        let result = await response.json();
        console.log(`Analizo de ${value}:`)
        for(const analizaĵo of result.analizaĵoj) {
            console.log(`- ${analizaĵo}`);
        }
    }
}

// ========================================

ReactDOM.render(
    <VortAnalizilo />,
    document.getElementById('radiko')
);
class VortAnalizilo extends React.Component {
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div id="input-container">
                    <label>
                        Analizota vorto:
                    </label>
                    <input
                        type="text"
                        id="word-input"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        required
                    />
                </div>
                <input type="submit" value="Analizi"/>
            </form>
        );
    }

    async handleSubmit(event) {
        event.preventDefault();
        let response = await fetch("api/msg");
        let message = await response.json();
        console.log(message.s);
    }
}

// ========================================

ReactDOM.render(
    <VortAnalizilo />,
    document.getElementById('radiko')
);
class FrazAnalizilo extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div id="fraz-analizilo">
                <h1>Frazo-Analizilo</h1>
                <InputForm
                    handleConfirm={this.handleConfirm.bind(this)}
                    label="Analizota frazo"
                    pattern="^(((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+(,? )?)+([\.!\?])?$"
                    handleInvalidity={this.handleInvalidity}
                    errorMessageForNoResults="Neniuj rezultoj montreblaj"
                />
            </div>
        );
    }

    handleConfirm() {
        return false;
    }

    handleInvalidity(value) {
        return "Enmetu Esperantan frazon";
    }
}
class FrazAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            frazo: null,
        }
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
                {this.renderFrazo()}
            </div>
        );
    }

    async handleConfirm(value) {
        let response = await fetch(`api/frazanalizo?frazo=${value}`);
        let result = await response.json();

        this.setState({
            frazo: result,
        });

        return result.frazeroj.length != 0;
    }

    handleInvalidity(value) {
        if(/^\s+$/.test(value)) {
            return "Enmetu Esperantan frazon";
        } else {
            return "Uzu nur Esperantajn literojn kaj spacetojn";
        }
    }

    renderFrazo() {
        return <Frazo key={this.state.frazo} frazo={this.state.frazo}/>
    }
}
class VortAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            analizaĵoj: null,
            vorto: null,
        }
    }

    render() {
        return (
            <div id="vort-analizilo">
                <h1>Vorto-Analizilo</h1>
                <InputForm
                    handleConfirm={this.handleConfirm.bind(this)}
                    label="Analizota vorto"
                    pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+(-((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+)*$"
                    handleInvalidity={this.handleInvalidity}
                    errorMessageForNoResults="Neniuj rezultoj montreblaj"
                />
                {this.renderAnalizaĵo()}
            </div>
        );
    }

    async handleConfirm(value) {
        let response = await fetch(`api/vortanalizo?vorto=${value}`);
        let result = await response.json();

        this.setState({
            analizaĵoj: result.analizaĵoj,
            vorto: value,
        });

        return result.analizaĵoj.length != 0;
    }

    handleInvalidity(value) {
        if(/\s/.test(value)) {
            return "Enmetu ekzakte unu vorton";
        } else {
            return "Uzu nur Esperantajn literojn";
        }
    }

    renderAnalizaĵo() {
        return <Analizaĵoj key={this.state.vorto} analizaĵoj={this.state.analizaĵoj}/>;
    }
}

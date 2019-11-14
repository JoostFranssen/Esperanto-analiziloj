class VortAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            analizaĵoj: null,
            vorto: null,
            validMesaĝo: null,
            ŝargante: false,
        }
    }

    render() {
        return (
            <div id="vort-analizilo">
                <h1>Vorto-Analizilo</h1>
                <form
                    onSubmit={(event) => this.traktiKonfirmon(event)}
                    id="vort-analizilo-form"
                    autocomplete="off"
                    novalidate
                >
                    <label id="vorto-etikedo" for="vort-enmeto">
                        Analizota vorto
                    </label>
                    <input
                        type="text"
                        name="vorto"
                        id="vort-enmeto"
                        autocomplete="off"
                        autoCapitalize="off"
                        spellcheck="false"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        autoFocus
                        novalidate
                        onInput={
                            (event) => {
                                let newState = Object.assign({}, this.state);
                                if(!event.target.validity.valid) {
                                    if(/\s/.test(event.target.value)) {
                                        newState.validMesaĝo = "Enmetu ekzakte unu vorton";
                                    } else {
                                        newState.validMesaĝo = "Uzu nur Esperantajn literojn"
                                    }
                                } else {
                                    newState.validMesaĝo = "";
                                }
                                this.setState(newState);
                            }
                        }
                        onInvalid={(event) => event.preventDefault()}
                    />
                    <input
                        type="submit"
                        value="Analizi"
                        id="konfirm-butono"
                    />
                    <span id="vort-validaĵo">{this.state.validMesaĝo}</span>
                    <span id="vort-ŝargikono" style={{display: this.state.ŝargante ? "block" : "none"}}></span>
                </form>
                {this.bildigiAnalizaĵojn()}
            </div>
        );
    }

    async traktiKonfirmon(event) {
        event.preventDefault();

        let newState = Object.assign({}, this.state);
        newState.ŝargante = true;
        this.setState(newState);

        let enmeto = document.getElementById("vort-enmeto");
        let response = await fetch(`api/vortanalizo?vorto=${enmeto.value}`);
        let result = await response.json();

        this.setState({
            analizaĵoj: result.analizaĵoj,
            vorto: enmeto.value,
            validMesaĝo: null,
            ŝargante: false,
        });
    }

    bildigiAnalizaĵojn() {
        return <Analizaĵoj key={this.state.vorto} analizaĵoj={this.state.analizaĵoj}/>;
    }
}

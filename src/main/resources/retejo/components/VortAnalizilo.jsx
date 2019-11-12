class VortAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            vortoj: null,
        }
    }

    render() {
        return (
            <div id="vort-analizilo">
                <h1>Vorto-Analizilo</h1>
                <form
                    onSubmit={(event) => this.traktiKonfirmon(event, document.getElementById("vort-enmeto").value)}
                    id="vort-analizilo-form"
                    autocomplete="off"
                >
                    <label id="vorto-etikedo">
                        Analizota vorto
                    </label>
                    <input
                        type="text"
                        name="vorto"
                        id="vort-enmeto"
                        autocomplete="off"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        required
                    />
                    <input
                        type="submit"
                        value="Analizi"
                        id="konfirm-butono"
                    />
                </form>
                {this.bildigiAnalizaĵojn()}
            </div>
        );
    }

    async traktiKonfirmon(event, value) {
        event.preventDefault();
        let response = await fetch(`api/vortanalizo?vorto=${value}`);
        let result = await response.json();
        
        this.setState({
            vortoj: result.analizaĵoj,
        });
    }

    bildigiAnalizaĵojn() {
        return <Analizaĵoj vortoj={this.state.vortoj}/>;
    }
}

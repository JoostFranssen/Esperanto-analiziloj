class Analizaĵoj extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedIndex: null,
            components: null,
        }
    }

    render() {
        return (
            <div className="vort-analizaĵujo">
                {this.createComponents()}
            </div>
        );
    }

    createComponents() {
        let componentList = [];
        if(this.props.analizaĵoj) {
            for(let i = 0; i < this.props.analizaĵoj.length; i++) {
                componentList.push(
                    <Analizaĵo
                        analizaĵo={this.props.analizaĵoj[i]}
                        selected={this.state.selectedIndex === i}
                        onClick={() => this.selectComponent(i)}
                    />
                );
            }
        }

        this.state.components = componentList;

        if(componentList.length > 0 && !this.state.selectedIndex) {
            this.selectComponent(0);
        }

        return componentList;
    }

    selectComponent(index) {
        let newState = Object.assign({}, this.state);

        if(index < 0 || !this.state.components || index >= this.state.components.length) {
            newState.selectedIndex = null;
        } else {
            newState.selectedIndex = index;
        }

        if(this.state.selectedIndex != index) {
            this.setState(newState);
        }
    }
}
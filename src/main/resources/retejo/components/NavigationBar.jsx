class NavigationBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            imageWidth: 0,
            selectedIndex: 0,
            componentProps: [
                {
                    title: "Vort-Analizilo",
                },
                {
                    title: "Fraz-Analizilo",
                }
            ],
        }
    }

    render() {
        return (
            <div
                className="navigation-bar"
            >
                <Banner width={this.state.imageWidth}/>
                {this.createComponents()}
            </div>
        );
    }

    clickHandler(event, index) {
        let newState = Object.assign({}, this.state);
        newState.selectedIndex = index;
        this.setState(newState);
    }

    createComponents() {
        let componentList = [];
        for(let i = 0; i < this.state.componentProps.length; i++) {
            let prop = this.state.componentProps[i];
            componentList.push(
                <NavigationButton
                    onClick={(event) => this.clickHandler(event, i)}
                    text={prop.title}
                    selected={i === this.state.selectedIndex}
                />
            );
        }
        return componentList;
    }

    componentDidMount() {
        let image = document.getElementById("icon");
        let newState = Object.assign({}, this.state);
        newState.imageWidth = image.style.height;
        this.setState(newState);
    }
}
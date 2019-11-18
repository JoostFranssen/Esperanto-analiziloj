class NavigationBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            imageWidth: 0,
            selectedIndex: (localStorage.getItem("selectedIndex") ? Number(localStorage.getItem("selectedIndex")) : 0),
            componentProps: [
                {
                    title: "Vort-Analizilo",
                },
                {
                    title: "Fraz-Analizilo",
                }
            ],
        }
        props.passNewSelected(this.state.selectedIndex);
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
        if(this.props.passNewSelected) {
            this.props.passNewSelected(index);
        }
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
        newState.imageWidth = image.offsetHeight;
        this.setState(newState);
    }
}
class NavigationBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            imageWidth: 0,
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

    clickHandler(event) {
        for(const component of this.state.componentList) {
            event.target.state.selected = (component == event.target);
        }
    }

    createComponents() {
        return [
            <NavigationButton
                onClick={this.clickHandler}
                text="Vort-Analizilo"
                selected={true}
            />,
            <NavigationButton
                onClick={this.clickHandler}
                text="Fraz-Analizilo"
                selected={false}
            />
        ]
    }

    componentDidMount() {
        let image = document.getElementById("icon");
        this.setState({imageWidth: image.style.height});
    }
}
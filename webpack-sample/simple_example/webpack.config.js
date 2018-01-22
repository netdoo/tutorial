module.exports = {
    devtool: 'eval-source-map',
    entry: {
        main : __dirname + "/main.js",
    },
    output: {
        path: __dirname + "/",
        filename: "bundle.js"
    },
    module: {
        loaders : [
            {  
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader', 
                query: {
                    presets:['env']
                }
            },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                loader: 'style-loader'
            },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                loader: 'css-loader'
            }
        ]
    },
    plugins: []
}

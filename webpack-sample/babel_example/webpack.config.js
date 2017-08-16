module.exports = {
    entry:__dirname + "/main.js",
    output: {
        path: __dirname + "/",
        filename: "bundle.js"
    },
    module:{ 
        loaders : [
          {
            test: /\.json$/,
            loader: 'json-loader'
          },
          {  
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader', 
            query: {
                presets:['es2015','react']
                }
            }
        ]
    }
}

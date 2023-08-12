'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  NODE_ENV_TITLE : '"Carlyle"',
  NODE_ENV_URL: '"http://localhost:8087"'
})

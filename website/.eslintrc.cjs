module.exports = {
  'ignorePatterns': [
    'node_modules/**/*',
    'dist/**/*',
  ],
  'env': {
    'browser': true,
    'es2021': true,
  },
  'extends': [
    'plugin:vue/vue3-essential',
    'google',
  ],
  'parserOptions': {
    'ecmaVersion': 'latest',
    'sourceType': 'module',
  },
  'plugins': [
    'vue',
  ],
  "rules": {
    'max-len': [
      'error',
      {
        'code': 120,
        'ignoreComments': true,
      },
    ],
    'semi': [
      'error',
      'never',
    ],
    'vue/multi-word-component-names': 'off',
  },
};

/**
 * Module dependencies.
 */
var passport = require('passport')
  , login = require('connect-ensure-login')


exports.index = function(req, res) {
  res.send('OAuth 2.0 Server');
};

exports.loginForm = function(req, res) {
  res.render('login');
};

exports.login = passport.authenticate('local', { successReturnToOrRedirect: '/', failureRedirect: '/login' });

exports.logout = function(req, res) {
  req.logout();
  res.redirect('/');
}

exports.public = function(req, res) {
  res.send('public area');
};

exports.private = [login.ensureLoggedIn(), function(req, res) {
  res.send('private area');
}];

exports.account = [
  login.ensureLoggedIn(),
  function(req, res) {
    res.render('account', { user: req.user });
  }
]

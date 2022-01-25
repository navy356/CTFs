var require = meteorInstall({
    "imports": {
        "ui": {
            "components": {
                "note": {
                    "note.html": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/components/note/note.html                                                                //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        module.link("./template.note.js", { "*": "*+" });

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "template.note.js": function() {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/components/note/template.note.js                                                         //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //

                        Template.__checkName("Note");
                        Template["Note"] = new Template("Template.Note", (function() {
                            var view = this;
                            return HTML.DIV({
                                class: "col s12 m6"
                            }, "\n        ", HTML.DIV({
                                class: "card white"
                            }, "\n        ", HTML.DIV({
                                class: "card-content"
                            }, "\n            ", Blaze.If(function() {
                                return Spacebars.call(view.lookup("editing"));
                            }, function() {
                                return ["\n                ", HTML.TEXTAREA({
                                    class: "materialize-textarea card-body-edit",
                                    style: "min-height: 125px;",
                                    placeholder: "Note",
                                    value: function() {
                                        return Spacebars.mustache(Spacebars.dot(view.lookup("note"), "body"));
                                    }
                                }), "\n            "];
                            }, function() {
                                return ["\n            ", HTML.DIV({
                                    class: "card-body"
                                }, "\n                ", Blaze.View("lookup:to_markdown", function() {
                                    return Spacebars.makeRaw(Spacebars.mustache(view.lookup("to_markdown"), Spacebars.dot(view.lookup("note"), "body")));
                                }), "\n            "), "\n            "];
                            }), "\n        "), "\n        ", HTML.Raw('<div class="card-action right-align">\n            <a class="card-delete-btn black-text" href="#">Delete</a>\n        </div>'), "\n        "), "\n    ");
                        }));

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "note.js": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/components/note/note.js                                                                  //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        let ReactiveVar;
                        module.link("meteor/reactive-var", {
                            ReactiveVar(v) {
                                ReactiveVar = v;
                            }

                        }, 0);
                        module.link("./note.html");
                        let Materialize;
                        module.link("materialize-css", {
                            default (v) {
                                Materialize = v;
                            }

                        }, 1);

                        const markdown = require('markdown').markdown;

                        Template.Note.onCreated(function() {
                            this.editing = new ReactiveVar(false);
                        });
                        Template.Note.helpers({
                            to_markdown(m) {
                                return markdown.toHTML(m);
                            },

                            editing() {
                                return Template.instance().editing.get();
                            }

                        });
                        Template.Note.events({
                            'click div.card-body': function(e, t) {
                                t.editing.set(!t.editing.get());
                                Tracker.afterFlush(function() {
                                    this.find('textarea').focus();
                                }.bind(t));
                            },
                            'change textarea.card-body-edit': function(e, t) {
                                t.data.note.body = e.currentTarget.value;
                            },
                            'focusout textarea.card-body-edit': function(e, t) {
                                if (e.currentTarget.value) {
                                    t.editing.set(!t.editing.get());
                                }
                            },
                            'click .card-delete-btn': function(e, t) {
                                Meteor.call('notes.remove', this.note._id, function(err, res) {
                                    if (err) {
                                        Materialize.toast({
                                            html: 'An error occurred: ' + err.toString(),
                                            displayLength: 3000
                                        });
                                    } else {
                                        Materialize.toast({
                                            html: 'Successfully deleted note!',
                                            displayLength: 3000
                                        });
                                    }
                                });
                            }
                        });
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    }
                }
            },
            "layouts": {
                "default": {
                    "default.html": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/default/default.html                                                             //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        module.link("./template.default.js", { "*": "*+" });

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "template.default.js": function() {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/default/template.default.js                                                      //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //

                        Template.__checkName("Layout_default");
                        Template["Layout_default"] = new Template("Template.Layout_default", (function() {
                            var view = this;
                            return HTML.DIV({
                                class: "background"
                            }, "\n        ", HTML.UL({
                                id: "slide-out",
                                class: "sidenav center"
                            }, "\n            ", HTML.LI(HTML.DIV({
                                class: "user-view center"
                            }, HTML.Raw('<i class="large material-icons">account_circle</i>'), "\n                ", HTML.SPAN({
                                class: "email"
                            }, Blaze.View("lookup:username", function() {
                                return Spacebars.mustache(view.lookup("username"));
                            }))), "\n            "), "\n            ", HTML.Raw('<li><div class="divider"></div></li>'), "\n            ", HTML.Raw('<li><a class="waves-effect" id="logout" href="#!">Sign out</a></li>'), "\n        "), HTML.Raw('\n        <div class="navbar-fixed">\n            <nav class="indigo">\n                <div class="nav-wrapper">\n                    <a href="#" class="brand-logo center"><i class="large material-icons">assignment</i>SecuriNotes</a>\n                    <ul id="nav-mobile" class="left">\n                        <li><a class="button-burger sidenav-trigger" href="#!" data-target="slide-out"><i class="material-icons">menu</i></a></li>\n                    </ul>\n                </div>\n            </nav>\n        </div>\n        '), HTML.DIV({
                                class: "container"
                            }, "\n            ", Blaze._TemplateWith(function() {
                                return {
                                    template: Spacebars.call(view.lookup("main"))
                                };
                            }, function() {
                                return Spacebars.include(function() {
                                    return Spacebars.call(Template.__dynamic);
                                });
                            }), "\n        "), "\n    ");
                        }));

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "default.js": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/default/default.js                                                               //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        let Meteor;
                        module.link("meteor/meteor", {
                            Meteor(v) {
                                Meteor = v;
                            }

                        }, 0);
                        let Template;
                        module.link("meteor/templating", {
                            Template(v) {
                                Template = v;
                            }

                        }, 1);
                        let FlowRouter;
                        module.link("meteor/kadira:flow-router", {
                            FlowRouter(v) {
                                FlowRouter = v;
                            }

                        }, 2);
                        module.link("./default.html");
                        let Materialize;
                        module.link("materialize-css", {
                            default (v) {
                                Materialize = v;
                            }

                        }, 3);
                        let $;
                        module.link("jquery", {
                            default (v) {
                                $ = v;
                            }

                        }, 4);
                        Template.Layout_default.onRendered(function() {
                            $(".sidenav").sidenav();
                        });
                        Template.Layout_default.helpers({
                            'username': function() {
                                var user = Meteor.user();
                                return user ? user.username : '';
                            }
                        });
                        Template.Layout_default.events({
                            'click #logout': function(e, t) {
                                Meteor.logout(function() {
                                    $('.sidenav').sidenav('close');
                                    FlowRouter.go('/login');
                                    Materialize.toast({
                                        html: "Logged out!",
                                        displayLength: 3000
                                    });
                                });
                            }
                        });
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    }
                },
                "login": {
                    "login.html": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/login/login.html                                                                 //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        module.link("./template.login.js", { "*": "*+" });

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "template.login.js": function() {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/login/template.login.js                                                          //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //

                        Template.__checkName("Layout_login");
                        Template["Layout_login"] = new Template("Template.Layout_login", (function() {
                            var view = this;
                            return HTML.DIV({
                                class: "background"
                            }, HTML.Raw('\n        <div class="navbar-fixed">\n            <nav class="indigo">\n                <div class="nav-wrapper">\n                    <a href="#" class="brand-logo center"><i class="large material-icons">assignment</i>SecuriNotes</a>\n                </div>\n            </nav>\n        </div>\n        '), HTML.DIV({
                                class: "container"
                            }, "\n            ", HTML.DIV({
                                class: "row add-margin-top"
                            }, "\n                ", HTML.DIV({
                                class: "col m12 l6 white-text drop-shadow"
                            }, "\n                    ", HTML.Raw("<h1>Welcome!</h1>"), "\n                    ", HTML.P("Our users are already trusting us with ", HTML.B(Blaze.View("lookup:notescount", function() {
                                return Spacebars.mustache(view.lookup("notescount"));
                            })), " notes!"), "\n                "), "\n                ", HTML.Raw('<div class="col m12 l6">\n                    <div class="card-panel">\n                        <div class="row">\n                            <div class="col s12">\n                                <ul class="tabs">\n                                    <li class="tab col s6"><a class="active indigo-text" href="#login">Login</a></li>\n                                    <li class="tab col s6"><a class="indigo-text" href="#signup">Sign up</a></li>\n                                </ul>\n                            </div>\n                            <div id="login" class="col s12">\n                                <form class="login-form">\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <i class="material-icons prefix">mail_outline</i>\n                                            <input class="validate" id="email" type="email">\n                                            <label for="email">Email</label>\n                                        </div>\n                                    </div>\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <i class="material-icons prefix">lock_outline</i>\n                                            <input id="password" type="password">\n                                            <label for="password">Password</label>\n                                        </div>\n                                    </div>\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <a href="#" id="btn-login" class="btn-large waves-effect waves-light indigo col s12">Login</a>\n                                        </div>\n                                    </div>\n                                </form>\n                            </div>\n                            <div id="signup" class="col s12">\n                                <form class="signup-form">\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <i class="material-icons prefix">mail_outline</i>\n                                            <input class="validate" id="signup-email" type="email">\n                                            <label for="signup-email">Email</label>\n                                        </div>\n                                    </div>\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <i class="material-icons prefix">lock_outline</i>\n                                            <input id="signup-password" type="password">\n                                            <label for="signup-password">Password</label>\n                                        </div>\n                                    </div>\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <i class="material-icons prefix">lock_outline</i>\n                                            <input id="signup-password-confirm" type="password">\n                                            <label for="signup-password-confirm">Confirm Password</label>\n                                        </div>\n                                    </div>\n                                    <div class="row">\n                                        <div class="input-field col s12">\n                                            <a href="#" id="btn-signup" class="btn-large waves-effect waves-light indigo col s12">Sign up</a>\n                                        </div>\n                                    </div>\n                                </form>\n                            </div>\n                        </div>\n                    </div>\n                </div>'), "\n            "), "\n        "), "\n    ");
                        }));

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "login.js": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/layouts/login/login.js                                                                   //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        let Meteor;
                        module.link("meteor/meteor", {
                            Meteor(v) {
                                Meteor = v;
                            }

                        }, 0);
                        let Accounts;
                        module.link("meteor/accounts-base", {
                            Accounts(v) {
                                Accounts = v;
                            }

                        }, 1);
                        let ReactiveVar;
                        module.link("meteor/reactive-var", {
                            ReactiveVar(v) {
                                ReactiveVar = v;
                            }

                        }, 2);
                        module.link("./login.html");
                        let Materialize;
                        module.link("materialize-css", {
                            default (v) {
                                Materialize = v;
                            }

                        }, 3);
                        let $;
                        module.link("jquery", {
                            default (v) {
                                $ = v;
                            }

                        }, 4);
                        Template.Layout_login.onCreated(function() {
                            var self = this;
                            this.notescount = new ReactiveVar(0);

                            function noteCountUpdater() {
                                Meteor.call('notes.count', {
                                    body: {
                                        $ne: ''
                                    }
                                }, function(err, res) {
                                    //console.log(res)
                                    self.notescount.set(res);
                                });
                            }

                            noteCountUpdater();
                            this.refresher = Meteor.setInterval(noteCountUpdater, 1000);
                        });
                        Template.Layout_login.onDestroyed(function() {
                            Meteor.clearInterval(this.refresher);
                        });
                        Template.Layout_login.onRendered(function() {
                            $('.tabs').tabs();
                        });
                        Template.Layout_login.helpers({
                            'notescount': function() {
                                return Template.instance().notescount.get();
                            }
                        });
                        Template.Layout_login.events({
                            'click #btn-login': function(e, t) {
                                var email = t.find('#email').value;
                                var password = t.find('#password').value;

                                if (!email || !password) {
                                    Materialize.toast({
                                        html: "Email and password required!",
                                        displayLength: 3000
                                    });
                                } else {
                                    Meteor.loginWithPassword({
                                        username: email
                                    }, password, function(err, res) {
                                        if (err) {
                                            Materialize.toast({
                                                html: "Login failed!",
                                                displayLength: 3000
                                            });
                                        } else {
                                            FlowRouter.go('/');
                                        }
                                    });
                                }
                            },
                            'click #btn-signup': function(e, t) {
                                var email = t.find('#signup-email').value;
                                var password = t.find('#signup-password').value;
                                var passwordConfirm = t.find('#signup-password-confirm').value;

                                if (password == passwordConfirm) {
                                    Accounts.createUser({
                                        username: email,
                                        password: password
                                    }, function(err, res) {
                                        if (err) {
                                            Materialize.toast({
                                                html: "Sign up failed!",
                                                displayLength: 3000
                                            });
                                            return;
                                        }

                                        Materialize.toast({
                                            html: "Registered!",
                                            displayLength: 3000
                                        });
                                        Meteor.loginWithPassword({
                                            username: email
                                        }, password, function(err, res) {
                                            if (err) {
                                                Materialize.toast({
                                                    html: "Login failed!",
                                                    displayLength: 3000
                                                });
                                                return;
                                            } else {
                                                FlowRouter.go('/');
                                            }
                                        });
                                    });
                                } else {
                                    Materialize.toast({
                                        html: "Passwords do not match!",
                                        displayLength: 3000
                                    });
                                }
                            }
                        });
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    }
                }
            },
            "pages": {
                "home": {
                    "home.html": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/pages/home/home.html                                                                     //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        module.link("./template.home.js", { "*": "*+" });

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "template.home.js": function() {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/pages/home/template.home.js                                                              //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //

                        Template.__checkName("Home");
                        Template["Home"] = new Template("Template.Home", (function() {
                            var view = this;
                            return [HTML.Raw('<h1 class="white-text drop-shadow ">Your notes</h1>\n    '), HTML.DIV({
                                class: "row"
                            }, "\n\n        ", Blaze.If(function() {
                                return Spacebars.call(view.lookup("no_notes"));
                            }, function() {
                                return ["\n        ", HTML.DIV({
                                    class: "white-text c12"
                                }, HTML.I("No notes to show")), "\n        "];
                            }), "\n\n        ", Blaze.Each(function() {
                                return {
                                    _sequence: Spacebars.call(view.lookup("notes")),
                                    _variable: "note"
                                };
                            }, function() {
                                return ["\n        ", Blaze._TemplateWith(function() {
                                    return {
                                        note: Spacebars.call(view.lookup("note"))
                                    };
                                }, function() {
                                    return Spacebars.include(view.lookupTemplate("Note"));
                                }), "\n        "];
                            }), "\n\n        ", HTML.Raw('<div class="fixed-action-btn">\n            <a class="btn-floating btn-large waves-effect waves-light red"><i class="material-icons">add</i></a>\n        </div>'), "\n\n    ")];
                        }));

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    },
                    "home.js": function(require, exports, module) {

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //                                                                                                     //
                        // imports/ui/pages/home/home.js                                                                       //
                        //                                                                                                     //
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        let Meteor;
                        module.link("meteor/meteor", {
                            Meteor(v) {
                                Meteor = v;
                            }

                        }, 0);
                        let Template;
                        module.link("meteor/templating", {
                            Template(v) {
                                Template = v;
                            }

                        }, 1);
                        let Notes;
                        module.link("../../../api/notes/notes.js", {
                            Notes(v) {
                                Notes = v;
                            }

                        }, 2);
                        module.link("../../../api/notes/methods.js");
                        module.link("../../components/note/note.js");
                        module.link("./home.html");
                        let Materialize;
                        module.link("materialize-css", {
                            default (v) {
                                Materialize = v;
                            }

                        }, 3);

                        const md = require('markdown').markdown;

                        Template.Home.onCreated(function() {
                            Meteor.subscribe('notes.private');
                        });
                        Template.Home.helpers({
                            notes: Notes.find(),
                            no_notes: function() {
                                return Notes.find().count() == 0;
                            },
                            user: function() {
                                return Meteor.user();
                            }
                        });
                        Template.Home.events({
                            'click .fixed-action-btn': function(e, t) {
                                Meteor.call('notes.add', function(err, res) {
                                    if (err) {
                                        Materialize.toast({
                                            html: 'An error occurred: ' + err.toString(),
                                            displayLength: 3000
                                        });
                                    } else {
                                        Materialize.toast({
                                            html: 'Successfully created a new note!',
                                            displayLength: 3000
                                        });
                                    }
                                });
                            }
                        });
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////

                    }
                }
            }
        },
        "api": {
            "notes": {
                "methods.js": function(require, exports, module) {

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //                                                                                                     //
                    // imports/api/notes/methods.js                                                                        //
                    //                                                                                                     //
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    let Meteor;
                    module.link("meteor/meteor", {
                        Meteor(v) {
                            Meteor = v;
                        }

                    }, 0);
                    let Notes;
                    module.link("./notes.js", {
                        Notes(v) {
                            Notes = v;
                        }

                    }, 1);
                    Meteor.methods({
                        'notes.count': function(filter) {
                            return Notes.find(filter).count();
                        },
                        'notes.add': function() {
                            let user = this.userId;

                            if (!user) {
                                throw new Meteor.Error('not-authorized', "You are not logged in.");
                            }

                            return Notes.insert({
                                body: "### Title\n\nNew note\n\nCreated at " + new Date().toLocaleString(),
                                owner: user
                            });
                        },
                        'notes.remove': function(id) {
                            let user = this.userId;

                            if (!user) {
                                throw new Meteor.Error('not-authorized', "You are not logged in.");
                            }

                            return Notes.remove({
                                _id: id,
                                owner: this.userId
                            });
                        }
                    });
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////

                },
                "notes.js": function(require, exports, module) {

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //                                                                                                     //
                    // imports/api/notes/notes.js                                                                          //
                    //                                                                                                     //
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    module.export({
                        Notes: () => Notes
                    });
                    const Notes = new Mongo.Collection('notes');
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////

                }
            }
        },
        "startup": {
            "client": {
                "index.js": function(require, exports, module) {

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //                                                                                                     //
                    // imports/startup/client/index.js                                                                     //
                    //                                                                                                     //
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    module.link("./routes.js");
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////

                },
                "routes.js": function(require, exports, module) {

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //                                                                                                     //
                    // imports/startup/client/routes.js                                                                    //
                    //                                                                                                     //
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //
                    let FlowRouter;
                    module.link("meteor/kadira:flow-router", {
                        FlowRouter(v) {
                            FlowRouter = v;
                        }

                    }, 0);
                    let BlazeLayout;
                    module.link("meteor/kadira:blaze-layout", {
                        BlazeLayout(v) {
                            BlazeLayout = v;
                        }

                    }, 1);
                    module.link("../../ui/layouts/default/default.js");
                    module.link("../../ui/layouts/login/login.js");
                    module.link("../../ui/pages/home/home.js");

                    const isAuthenticatedRedirect = (context, redirect, stop) => {
                        if (!Meteor.loggingIn() && !Meteor.userId()) {
                            redirect('/login');
                        }
                    };

                    const authRoutes = FlowRouter.group({
                        name: 'isAuthenticated',
                        triggersEnter: [isAuthenticatedRedirect]
                    });
                    const exposedRoutes = FlowRouter.group({
                        name: 'exposed'
                    });
                    exposedRoutes.route('/login', {
                        name: 'App.login',

                        action() {
                            BlazeLayout.render('Layout_login');
                        }

                    });
                    authRoutes.route('/', {
                        name: 'App.home',

                        action() {
                            BlazeLayout.render('Layout_default', {
                                main: 'Home'
                            });
                        }

                    });
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////

                }
            }
        }
    },
    "client": {
        "main.js": function(require, exports, module) {

            /////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                                                                                     //
            // client/main.js                                                                                      //
            //                                                                                                     //
            /////////////////////////////////////////////////////////////////////////////////////////////////////////
            //
            module.link("/imports/startup/client");
            /////////////////////////////////////////////////////////////////////////////////////////////////////////

        }
    }
}, {
    "extensions": [
        ".js",
        ".json",
        ".html",
        ".scss",
        ".css"
    ]
});

var exports = require("/client/main.js");
package com.gethighlow.highlowandroid.model.Managers.LiveDataModels;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import com.gethighlow.highlowandroid.model.util.Consumer;
import com.gethighlow.highlowandroid.model.Resources.Comment;
import com.gethighlow.highlowandroid.model.Resources.HighLow;
import com.gethighlow.highlowandroid.model.Services.HighLowService;
import com.gethighlow.highlowandroid.model.util.Runnable;

import java.util.ArrayList;
import java.util.List;

public class HighLowLiveData extends MutableLiveData<HighLow> {
    private HighLow highLow;

    public HighLowLiveData(HighLow highLow) {
        super();
        this.highLow = highLow;
        this.setValue(highLow);
    }

    public HighLow getHighLow() {
        return this.getValue();
    }

    public void update(Runnable onSuccess) {
        HighLow highLow = getValue();
        if (highLow == null) {
            onSuccess.run();
            return;
        }

        if (highLow.getHighlowid() != null) {
            HighLowService.shared().get(highLow.getHighlowid(), new Consumer<HighLow>() {
                @Override
                public void accept(HighLow value) {
                    HighLowLiveData.this.setValue(value);
                    onSuccess.run();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    onSuccess.run();
                }
            });
        } else if (highLow.getDate() != null) {
            HighLowService.shared().getDate(highLow.getDate(), new Consumer<HighLow>() {
                @Override
                public void accept(HighLow value) {
                    HighLowLiveData.this.setValue(value);
                    onSuccess.run();
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                    onSuccess.run();
                }
            });
        } else {
            onSuccess.run();
        }
    }


    public void update() {
        HighLow highLow = getValue();
        if (highLow == null) {
            return;
        }

        if (highLow.getHighlowid() != null) {
            HighLowService.shared().get(highLow.getHighlowid(), new Consumer<HighLow>() {
                @Override
                public void accept(HighLow value) {
                    HighLowLiveData.this.setValue(value);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                }
            });
        } else if (highLow.getDate() != null) {
            HighLowService.shared().getDate(highLow.getDate(), new Consumer<HighLow>() {
                @Override
                public void accept(HighLow value) {
                    HighLowLiveData.this.setValue(value);
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String error) {
                }
            });
        }
    }

    public void setHigh(String high, String date, Boolean isPrivate, Bitmap image, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.setHigh(high, date, isPrivate, image, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void setLow(String low, String date, Boolean isPrivate, Bitmap image, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.setLow(low, date, isPrivate, image, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void makePrivate(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.makePrivate(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void makePublic(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.makePublic(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void like(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.like(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void unLike(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.unLike(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void flag(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.flag(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void unFlag(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.unFlag(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void comment(String message, final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.comment(message, new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public void getComments(final Consumer<HighLow> onSuccess, Consumer<String> onError) {
        final HighLow highLow = getValue();
        if (highLow == null) {
            onError.accept("does-not-exist");
        }
        highLow.getComments(new Consumer<HighLow>() {
            @Override
            public void accept(HighLow newHighLow) {
                HighLowLiveData.this.setValue(highLow);
                onSuccess.accept(highLow);
            }
        }, onError);
    }

    public List<CommentLiveData> getComments() {
        HighLow highLow = getValue();
        List<CommentLiveData> comments = new ArrayList<CommentLiveData>();

        for (Comment comment: highLow.getComments()) {
            comments.add(new CommentLiveData(comment));
        }

        return comments;
    }

    public String getHighlowid() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHighlowid();
    }

    public String getHigh() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHigh();
    }

    public String getHighImage() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getHighImage();
    }

    public String getLow() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getLow();
    }

    public String getLowImage() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getLowImage();
    }

    public Boolean getLiked() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getLiked();
    }

    public Boolean getFlagged() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getFlagged();
    }

    public Integer getTotalLikes() {
        HighLow highLow = getValue();
        if (highLow == null) return 0;
        return highLow.getTotal_likes();
    }

    public Boolean getPrivate() {
        HighLow highLow = getValue();
        if (highLow == null) return false;
        return highLow.getPrivate();
    }

    public String getUid() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getUid();
    }

    public String getDate() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getDate();
    }

    public String getTimestamp() {
        HighLow highLow = getValue();
        if (highLow == null) return null;
        return highLow.getTimestamp();
    }
}
